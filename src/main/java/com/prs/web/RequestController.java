package com.prs.web;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;


import com.prs.business.Request;
import com.prs.db.RequestRepository;


@CrossOrigin
@RestController
@RequestMapping("/requests")
public class RequestController {
	@Autowired
	private RequestRepository requestRepo;

	// list - return all requests
	@GetMapping("/")
	public JsonResponse listRequests() {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(requestRepo.findAll());
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// get - return 1 request for the given id
	@GetMapping("/{id}")
	public JsonResponse getRequest(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(requestRepo.findById(id));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// add - adds a new Request
	@PostMapping("/")
	public JsonResponse addRequest(@RequestBody Request r) {
		// add a new Request
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (DataIntegrityViolationException dive) {
			jr = JsonResponse.getInstance(dive.getRootCause().getMessage());
			dive.printStackTrace();
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// update - update a new Request
	@PutMapping("/")
	public JsonResponse updateRequest(@RequestBody Request r) {
		// update a request
		JsonResponse jr = null;
		try {
			if (requestRepo.existsById(r.getId())) {
				jr = JsonResponse.getInstance(requestRepo.save(r));
			} else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error updating Request.  id: " + r.getId() + " doesn't exist!");
			}
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// delete - delete a Request
	@DeleteMapping("/{id}")
	public JsonResponse deleteRequest(@PathVariable int id) {
		// delete a request
		JsonResponse jr = null;
		try {
			if (requestRepo.existsById(id)) {
				requestRepo.deleteById(id);
				;
				jr = JsonResponse.getInstance("Delete succesful!");
			} else {
				// record doesn't exist
				jr = JsonResponse.getInstance("Error deleting Request. id: " + id + " doesn't exist!");
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
