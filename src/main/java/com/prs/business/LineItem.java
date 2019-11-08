package com.prs.business;

import javax.persistence.*;

@Entity
public class LineItem {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;
	@ManyToOne
	@JoinColumn(name="RequestID")
	private Request request;
	@ManyToOne
	@JoinColumn(name="ProductID")
	private Product product;
	private int quantity;
	
	public LineItem() {
		super();
	}

	public LineItem(int id, Request request, Product product, int quantity) {
		super();
		this.id = id;
		this.request = request;
		this.product = product;
		this.quantity = quantity;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Request getRequest() {
		return request;
	}

	public void setRequest(Request request) {
		this.request = request;
	}

	public Product getProduct() {
		return product;
	}

	public void setProduct(Product product) {
		this.product = product;
	}

	public int getQuantity() {
		return quantity;
	}

	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}

	@Override
	public String toString() {
		return "LineItem [id=" + id + ", requestId=" + request + ", productId=" + product + ", quantity=" + quantity
				+ "]";
	}
	
}
