<%@ page language="java" import="java.util.*" pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html xmlns="http://www.w3.org/1999/xhtml">
<head>
<meta http-equiv="X-UA-Compatible" content="IE=emulateIE7" />
<meta http-equiv="Content-Type" content="text/html; charset=utf-8" />
<link rel="stylesheet" type="text/css" href="css/style.css" />
<link rel="stylesheet" type="text/css" href="css/SimpleTree.css"/>
<link rel="stylesheet" type="text/css" href="css/skin_/nav.css" />
<link rel="stylesheet" type="text/css" href="css/WdatePicker.css" />
<link rel="stylesheet" type="text/css" href="css/skin_/table.css" />
<link rel="stylesheet" type="text/css" href="css/jquery.grid.css" />


<title>保单管理</title>
</head>

<body>

<div id="container">
	<div id="bd">
    	<div class="sidebar">
            <div id="tree" class="st_tree" style="width:400px;margin:0 auto;">
	     
	
     </div>
        </div>

    </div>
</div>


<div id="container">
	<div id="hd"></div>
    <div id="bd">
    	<div id="main">
        	<div class="search-box ue-clear" style="height: 100px;">
            	<div class="search-area">
                    <div class="kv-item ue-clear">
                        <label>选择时间：</label>
                        <div class="kv-item-content ue-clear">
                            <span class="choose">
                                <span class="checkboxouter">
                                    <input type="radio" name="time" checked />
                                    <span class="radio"></span>
                                </span>
                                <span class="text">全部</span>
                            </span>
                            <span class="choose">
                                <span class="checkboxouter">
                                    <input type="radio" name="time" />
                                    <span class="radio"></span>
                                </span>
                                <span class="text">近3天</span>
                            </span>
                            <span class="choose">
                                <span class="checkboxouter">
                                    <input type="radio" name="time" />
                                    <span class="radio"></span>
                                </span>
                                <span class="text">近一周</span>
                            </span>
                            <span class="choose">
                                <span class="checkboxouter">
                                    <input type="radio" name="time" />
                                    <span class="radio"></span>
                                </span>
                                <span class="text">近一月</span>
                            </span>
                            
                            <span class="choose">
									<span class="checkboxouter">
										<input type="radio" name="time" data-define="define" />
										<span class="radio"></span>
									</span>
									<span class="text">自定义</span>
								</span>
								<span class="define-input">
									<input type="text" placeholder="开始时间" />
									<span class="division"></span>
									<input type="text" placeholder="结束时间" />
								</span>
                        </div>
                    </div>
                    <div class="kv-item ue-clear" >
                        <label>旅游团类型:</label>
                        <div class="kv-item-content" >
                            <select  style="width:200px;">
                                <option>省内</option>
                            <option>省外（不含港澳台）</option>
                            <option>境外（含港澳台）</option>
							<option>边境游</option>
                            </select>
                        </div>
                    </div>
					<div class="kv-item ue-clear" >
                        <label>保单状态:</label>
                        <div class="kv-item-content" >
                            <select  style="width:200px;">
							<option value="">请选择状态</option>
                            <option>待付款</option>
                            <option>已付款待生效</option>
							<option>已生效</option>
							<option>已完成</option>
							<option>已撤单</option>
                            </select>
                        </div>
                    </div>
                </div>
                <div class="search-button" align="right">
                	<input class="button" type="button" value="查询" />
                </div>
             </div>
			 
			 <div  style="font-size:16px;color: red;text-align: left">折扣  旅责险：9.2；意外险：9.4</div>
			 <div class="ui-table ui-table-fixed-checkbox">
				<table style="margin-left:0px;width:100%">
				     <thead>
					 <tr>
					 <th name="id" style="width:10%"><div class="ui-table-th" >单号</div></th>
					 <th name="name"  style="width:10%"><div class="ui-table-th">类型</div></th>
					 <th style="width:8%"><div class="ui-table-th"><span class="ui-table-thTitle">旅责险</span></div></th>
					 <th style="width:8%"><div class="ui-table-th"><span class="ui-table-thTitle">意外险</span></div></th>
					 <th style="width:8%"><div class="ui-table-th"><span class="ui-table-thTitle">保费总额</span></div></th>
					 <th style="width:8%"><div class="ui-table-th"><span class="ui-table-thTitle">应付保费</span></div></th>
					 <th  style="width:15%"><div class="ui-table-th"><span class="ui-table-thTitle">保单时间</span></div></th>
					 <th  style="width:8%"><div class="ui-table-th"><span class="ui-table-thTitle">状态</span></div></th>
					 <th  style="width:10%"><div class="ui-table-th"><span class="ui-table-thTitle">录入时间</span></div></th>
					 <th  style="width:19%"><div class="ui-table-th"><span class="ui-table-thTitle">操作</span></div></th>
					 </tr>
					 </thead>
					 <tbody>
						<tr>
						<td name="id" style="width:10%"><a>201301</a></td>
						<td name="name"  style="width:10%"><div class="ui-table-td">省内</div></td>
						<td  style="width:8%">20.00</td>
						<td style="width:8%">35.00</td>
						<td style="width:8%">65</td>
						<td style="width:8%">55.00</td>
						<td  style="width:15%">2019-05-01 00:00:00</br>2019-05-03 23:59:59</td>
						<td style="width:8%"> 待付费</td>
						<td  style="width:10%">2019-05-03 23:59:59</td>
					    <td style="width: 19%"><a>[付款]</a>  <a href="policyedit.html">[编辑]</a> <a>[撤销]</a></td>
						</tr>
					    <tr>
						<td name="id" style="width:10%"><a>201301</a></td>
						<td name="name"  style="width:10%"><div class="ui-table-td">省内</div></td>
						<td  style="width:8%">20.00</td>
						<td style="width:8%">35.00</td>
						<td style="width:8%">65</td>
						<td style="width:8%">55.00</td>
						<td  style="width:15%">2019-05-01 00:00:00</br>2019-05-03 23:59:59</td>
						<td style="width:8%"> 已付费待生效</td>
						<td  style="width:10%">2019-05-03 23:59:59</td>
					    <td style="width: 19%"><a href="policyedit_pay.html">[已付费订单修改]</a> <a>[撤销]</a></td>
						</tr>
						<tr>
						<td name="id" style="width:10%"><a>201301</a></td>
						<td name="name"  style="width:10%"><div class="ui-table-td">省内</div></td>
						<td  style="width:8%">20.00</td>
						<td style="width:8%">35.00</td>
						<td style="width:8%">65</td>
						<td style="width:8%">55.00</td>
						<td  style="width:15%">2019-05-01 00:00:00</br>2019-05-03 23:59:59</td>
						<td style="width:8%">已生效</td>
						<td  style="width:10%">2019-05-03 23:59:59</td>
					    <td style="width: 19%"><a href="policyedit_effect.html">[已生效订单修改]</a> </td>
						</tr>
						<tr>
						<td name="id" style="width:10%"><a>201301</a></td>
						<td name="name"  style="width:10%"><div class="ui-table-td">省内</div></td>
						<td  style="width:8%">20.00</td>
						<td style="width:8%">35.00</td>
						<td style="width:8%">65</td>
						<td style="width:8%">55.00</td>
						<td  style="width:15%">2019-05-01 00:00:00</br>2019-05-03 23:59:59</td>
						<td style="width:8%">已完成</td>
						<td  style="width:10%">2019-05-03 23:59:59</td>
					    <td style="width: 19%"></td>
						</tr>
						<tr>
						<td name="id" style="width:10%"><a>201301</a></td>
						<td name="name"  style="width:10%"><div class="ui-table-td">省内</div></td>
						<td  style="width:8%">20.00</td>
						<td style="width:8%">35.00</td>
						<td style="width:8%">65</td>
						<td style="width:8%">55.00</td>
						<td  style="width:15%">2019-05-01 00:00:00</br>2019-05-03 23:59:59</td>
						<td style="width:8%">已撤销</td>
						<td  style="width:10%">2019-05-03 23:59:59</td>
					    <td style="width: 19%"></td>
						</tr>
						</tbody>
					 </table>
				 </div>
	</div>
</body>
<script type="text/javascript" src="js/jquery-1.6.min.js"></script>
<script type="text/javascript" src="js/SimpleTree.js"></script>
<script type="text/javascript" src="js/jquery.js"></script>
<script type="text/javascript" src="js/global.js"></script>
<script type="text/javascript" src="js/jquery.select.js"></script>
<script type="text/javascript" src="js/core.js"></script>
<script type="text/javascript" src="js/jquery.pagination.js"></script>
<script type="text/javascript" src="js/jquery.grid.js"></script>
<script type="text/javascript" src="js/WdatePicker.js"></script>
<script type="text/javascript">	
	$('.search-box input[type=radio]').click(function(e) {
        if($(this).prop('checked')){
			if($(this).attr('data-define') === 'define'){
				$('.define-input').show();
			}else{
				$('.define-input').hide();
			}
		}
    });
</script><script type="text/javascript">
$(function(){
console.log("tree init");
 $.ajax({
        async : false,    //表示请求是否异步处理
        type : "post",    //请求类型
        url : "/user/getorgans",//请求的 URL地址
        dataType : "json",//返回的数据类型
        
        success: function (data) {
          console.log(data);  //在控制台打印服务器端返回的数据
           if(data.code==200)
           {
           $('#tree').append(data.result);
           $(".st_tree").SimpleTree({
	
	
	 
	
	
	
	
		
		click:function(a){
		 	 
			 
			 
			console.log($(a).attr("ref"));
				 
		}
		
	});           }
           else
           {
             alert(data.message);
           }
      
        },
        error:function (data) {
            alert("输入有误");
        }
    });
	
	
});
</script>
</html>
