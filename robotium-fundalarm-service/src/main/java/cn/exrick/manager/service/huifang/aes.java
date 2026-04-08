package cn.exrick.manager.service.huifang;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class aes {
	private static final String ALGORITHM = "DES";

	public static Key getRawKey(String str)
			throws InvalidKeyException, InvalidKeySpecException, NoSuchAlgorithmException {

		return SecretKeyFactory.getInstance(ALGORITHM).generateSecret(new DESKeySpec(str.getBytes()));

	}

	public static void main(String[] args) {
		// Key key =
		// getRawKey("9GgjZ4Z+fT99OhyP2cOTvybkGvakK7a8oHlWSmMebLl9hYFo7Hpnpp9ldM9CY0j4");

	}

}
