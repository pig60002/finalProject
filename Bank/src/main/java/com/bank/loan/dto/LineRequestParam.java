package com.bank.loan.dto;


import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LineRequestParam {

	private Integer amount;
	private String currency = "TWD";
	private Integer orderId;
	private RedirectUrls redirectUrls;
	private List<Package> packages = new ArrayList<>();

	@Getter
	@Setter
	public static class RedirectUrls {
		private String confirmUrl;
		private String cancelUrl;
	}

	@Getter
	@Setter
	public static class Package {
		private Integer amount;
		private String id;
		private String name;
		private List<Prdocut> products = new ArrayList<>();
	}

	@Getter
	@Setter
	public static class Prdocut {
		private String id;
		private String imageUrl;
		private String name;
		private Integer price;
		private Integer quantity;
	}
}
