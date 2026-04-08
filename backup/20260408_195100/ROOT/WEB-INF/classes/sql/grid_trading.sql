-- =============================================
-- OKX 网格交易 + 智能做T 数据库设计
-- =============================================

-- 1. 网格配置表（预设网格/无限网格）
CREATE TABLE IF NOT EXISTS `grid_config` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `symbol` VARCHAR(50) NOT NULL COMMENT '交易对，如 BTC-USDT-SWAP',
    `grid_type` TINYINT NOT NULL DEFAULT 1 COMMENT '1:等差网格 2:等比网格 3:无限网格',
    `upper_price` DECIMAL(18,8) NOT NULL COMMENT '网格上限价格',
    `lower_price` DECIMAL(18,8) NOT NULL COMMENT '网格下限价格',
    `grid_count` INT NOT NULL COMMENT '网格数量',
    `grid_spacing` DECIMAL(8,6) COMMENT '网格间距（等比时用）',
    `grid_spacing_value` DECIMAL(18,8) COMMENT '网格间距值（等差时用）',
    `invest_amount` DECIMAL(18,8) NOT NULL COMMENT '投入金额',
    `base_position_pct` DECIMAL(4,2) DEFAULT 0.50 COMMENT '底仓占比',
    `t_position_pct` DECIMAL(4,2) DEFAULT 0.30 COMMENT '做T仓位占比',
    
    -- 无限网格特有参数
    `is_infinite` TINYINT DEFAULT 0 COMMENT '是否无限网格',
    `expand_upper_ratio` DECIMAL(4,2) DEFAULT 0.10 COMMENT '上限突破后扩展比例',
    `expand_lower_ratio` DECIMAL(4,2) DEFAULT 0.10 COMMENT '下限突破后扩展比例',
    `max_expansions` INT DEFAULT 3 COMMENT '最大扩展次数',
    
    -- 风控参数
    `stop_loss_pct` DECIMAL(5,2) DEFAULT 0.05 COMMENT '止损百分比',
    `take_profit_pct` DECIMAL(5,2) DEFAULT 0.10 COMMENT '止盈百分比',
    `max_t_loss_pct` DECIMAL(5,2) DEFAULT 0.01 COMMENT '做T最大亏损',
    
    `status` TINYINT DEFAULT 1 COMMENT '1:运行中 0:暂停 -1:已停止',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    UNIQUE KEY `uk_symbol` (`symbol`),
    INDEX `idx_status` (`status`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格配置表';

-- 2. 网格档位表（记录每个网格的状态）
CREATE TABLE IF NOT EXISTS `grid_levels` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `config_id` BIGINT NOT NULL COMMENT 'grid_config.id',
    `level_index` INT NOT NULL COMMENT '档位序号（从0开始）',
    `buy_price` DECIMAL(18,8) NOT NULL COMMENT '买入触发价',
    `sell_price` DECIMAL(18,8) NOT NULL COMMENT '卖出触发价',
    `grid_amount` DECIMAL(18,8) NOT NULL COMMENT '每格交易数量',
    `position_amount` DECIMAL(18,8) DEFAULT 0 COMMENT '当前持仓数量',
    
    -- 档位状态
    `status` TINYINT DEFAULT 0 COMMENT '-1:已跳过 0:空仓等待买入 1:持仓等待卖出 2:已完成',
    `is_t_grid` TINYINT DEFAULT 0 COMMENT '是否做T专用档位',
    
    -- 成交记录
    `buy_order_id` VARCHAR(100) COMMENT '买入订单ID',
    `buy_fill_price` DECIMAL(18,8) COMMENT '实际买入价格',
    `buy_fill_time` DATETIME COMMENT '买入成交时间',
    `sell_order_id` VARCHAR(100) COMMENT '卖出订单ID',
    `sell_fill_price` DECIMAL(18,8) COMMENT '实际卖出价格',
    `sell_fill_time` DATETIME COMMENT '卖出成交时间',
    
    `profit` DECIMAL(18,8) DEFAULT 0 COMMENT '该格盈利',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    UNIQUE KEY `uk_config_level` (`config_id`, `level_index`),
    INDEX `idx_status` (`status`),
    FOREIGN KEY (`config_id`) REFERENCES `grid_config`(`id`) ON DELETE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格档位表';

-- 3. 做T交易记录表（单独的做T记录，防止与网格混乱）
CREATE TABLE IF NOT EXISTS `t_trade_record` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `symbol` VARCHAR(50) NOT NULL,
    `config_id` BIGINT COMMENT '关联的网格配置',
    
    -- 做T方向
    `t_direction` TINYINT NOT NULL COMMENT '1:正T（先买后卖） -1:反T（先卖后买）',
    `t_type` VARCHAR(20) COMMENT 'SIGNAL:信号驱动 GRID:网格做T',
    
    -- 买入记录
    `buy_order_id` VARCHAR(100),
    `buy_price` DECIMAL(18,8),
    `buy_amount` DECIMAL(18,8),
    `buy_time` DATETIME,
    `buy_fill_price` DECIMAL(18,8),
    
    -- 卖出记录
    `sell_order_id` VARCHAR(100),
    `sell_price` DECIMAL(18,8),
    `sell_amount` DECIMAL(18,8),
    `sell_time` DATETIME,
    `sell_fill_price` DECIMAL(18,8),
    
    -- 盈亏统计
    `profit` DECIMAL(18,8) DEFAULT 0 COMMENT '做T收益',
    `profit_pct` DECIMAL(8,4) COMMENT '收益率',
    `holding_time_minutes` INT COMMENT '持仓时长（分钟）',
    
    -- 状态
    `status` TINYINT DEFAULT 0 COMMENT '0:买入未成交 1:持仓中 2:已完成 -1:已止损',
    `close_reason` VARCHAR(50) COMMENT 'NORMAL:正常止盈止损 STOP_LOSS:止损熔断',
    
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    
    INDEX `idx_symbol_status` (`symbol`, `status`),
    INDEX `idx_config` (`config_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='做T交易记录表';

-- 4. 持仓汇总表（实时统计）
CREATE TABLE IF NOT EXISTS `position_summary` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `symbol` VARCHAR(50) NOT NULL UNIQUE,
    
    -- 总持仓
    `total_amount` DECIMAL(18,8) DEFAULT 0 COMMENT '总持仓数量',
    `total_cost` DECIMAL(18,8) DEFAULT 0 COMMENT '总成本',
    `avg_cost` DECIMAL(18,8) DEFAULT 0 COMMENT '平均成本',
    `market_value` DECIMAL(18,8) DEFAULT 0 COMMENT '市值',
    `unrealized_pnl` DECIMAL(18,8) DEFAULT 0 COMMENT '未实现盈亏',
    
    -- 分离统计
    `base_amount` DECIMAL(18,8) DEFAULT 0 COMMENT '底仓数量',
    `base_cost` DECIMAL(18,8) DEFAULT 0 COMMENT '底仓成本',
    `t_position_amount` DECIMAL(18,8) DEFAULT 0 COMMENT '做T持仓数量（正T买入未卖）',
    `t_position_cost` DECIMAL(18,8) DEFAULT 0 COMMENT '做T持仓成本',
    `t_short_amount` DECIMAL(18,8) DEFAULT 0 COMMENT '做T空头数量（反T卖出未买）',
    `t_short_avg_price` DECIMAL(18,8) DEFAULT 0 COMMENT '反T卖出均价',
    
    -- 今日统计
    `today_t_count` INT DEFAULT 0 COMMENT '今日做T次数',
    `today_t_profit` DECIMAL(18,8) DEFAULT 0 COMMENT '今日做T收益',
    
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='持仓汇总表';

-- 5. 网格扩展记录（无限网格用）
CREATE TABLE IF NOT EXISTS `grid_expansion` (
    `id` BIGINT PRIMARY KEY AUTO_INCREMENT,
    `config_id` BIGINT NOT NULL,
    `expansion_no` INT NOT NULL COMMENT '第几次扩展',
    `direction` TINYINT COMMENT '1:向上扩展 -1:向下扩展',
    `old_upper` DECIMAL(18,8),
    `old_lower` DECIMAL(18,8),
    `new_upper` DECIMAL(18,8),
    `new_lower` DECIMAL(18,8),
    `trigger_price` DECIMAL(18,8) COMMENT '触发扩展的价格',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP,
    
    FOREIGN KEY (`config_id`) REFERENCES `grid_config`(`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COMMENT='网格扩展记录';

-- =============================================
-- 初始化示例数据
-- =============================================

-- 示例：BTC 无限网格配置
INSERT IGNORE INTO `grid_config` (
    `symbol`, `grid_type`, `upper_price`, `lower_price`, `grid_count`,
    `grid_spacing`, `invest_amount`, `base_position_pct`, `t_position_pct`,
    `is_infinite`, `expand_upper_ratio`, `expand_lower_ratio`, `max_expansions`
) VALUES (
    'BTC-USDT-SWAP', 3, 70000.00, 60000.00, 50,
    0.002, 10000.00, 0.50, 0.30,
    1, 0.10, 0.10, 3
);

-- 示例：ETH 等差网格配置
INSERT IGNORE INTO `grid_config` (
    `symbol`, `grid_type`, `upper_price`, `lower_price`, `grid_count`,
    `grid_spacing_value`, `invest_amount`, `base_position_pct`, `t_position_pct`,
    `is_infinite`, `stop_loss_pct`, `max_t_loss_pct`
) VALUES (
    'ETH-USDT-SWAP', 1, 4000.00, 3000.00, 40,
    25.00, 5000.00, 0.50, 0.30,
    0, 0.05, 0.01
);
