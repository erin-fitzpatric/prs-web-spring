package com.prs.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.LineItem;
import com.prs.business.Product;
import com.prs.business.Request;
import com.prs.db.LineItemRepository;
import com.prs.db.RequestRepository;

@CrossOrigin
@RestController
@RequestMapping("/line-items")

public class LineItemController {
	@Autowired
	private LineItemRepository lineItemRepo;
	@Autowired
	private RequestRepository requestRepo;
	
	// list - return all lineItems
	@GetMapping("/")
	public JsonResponse listLineItems() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// get - return 1 lineItem for the given id
	@GetMapping("/{id}")
	public JsonResponse getLineItem(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findById(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// get - list line items for a PR 
	@GetMapping("/lines-for-pr/{prId}")
	public JsonResponse getAllLineItems(@PathVariable int prId) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.findByRequestId(prId));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// add - adds a new lineItem
	@PostMapping("/")
	public JsonResponse addLineItem(@RequestBody LineItem l) {
		// add a new lineItem
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.save(l));
			recalculateTotal(l.getRequest().getId());
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// update - update a new LineItem
	@PutMapping("/")
	public JsonResponse updateLineItem(@RequestBody LineItem l) {
		// update a lineItem
		JsonResponse jr = null;
		try {
			if (lineItemRepo.existsById(l.getId())) {
				jr = JsonResponse.getInstance(lineItemRepo.save(l));
				recalculateTotal(l.getRequest().getId());
			} else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error updating Line Item.  id: " + l.getId() + " doesn't exist!");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// delete - delete a LineItem
	@DeleteMapping("/{id}")
	public JsonResponse deleteLineItem(@PathVariable int id) {
		// delete a lineItem
		JsonResponse jr = null;
		try {
			if (lineItemRepo.existsById(id)) {
				LineItem l = lineItemRepo.findById(id).get();
				lineItemRepo.deleteById(id);
				recalculateTotal(l.getRequest().getId());
				jr = JsonResponse.getInstance("Delete succesful!");
			} else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error deleting Line Item. id: " + id + " doesn't exist!");
			}
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	private void recalculateTotal(int requestID) {
		double total = 0.0;
		Request r = requestRepo.findById(requestID).get();
		List<LineItem> lines = lineItemRepo.findByRequestId(requestID);
		for (LineItem line : lines) {
			Product p = line.getProduct();
			total += p.getPrice()*line.getQuantity();
		}
		r.setTotal(total);
		requestRepo.save(r);
	}
}
