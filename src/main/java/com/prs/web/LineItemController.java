package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.LineItem;
import com.prs.db.LineItemRepository;


@CrossOrigin
@RestController
@RequestMapping("/line-items")

public class LineItemController {
	@Autowired
	private LineItemRepository lineItemRepo;

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

	// add - adds a new lineItem
	@PostMapping("/")
	public JsonResponse addLineItem(@RequestBody LineItem l) {
		// add a new lineItem
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(lineItemRepo.save(l));
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
				lineItemRepo.deleteById(id);
				;
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
}
