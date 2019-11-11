package com.prs.web;

import java.time.LocalDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.web.bind.annotation.*;

import com.prs.business.Request;
import com.prs.db.RequestRepository;
import com.prs.db.UserRepository;

@CrossOrigin
@RestController
@RequestMapping("/requests")
public class RequestController {
	@Autowired
	private RequestRepository requestRepo;
	@Autowired
	private UserRepository userRepo;

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

	// Get - request review 
	@GetMapping("/list-view/{id}")
	public JsonResponse reviewRequest(@PathVariable int id) {
		JsonResponse jr = null;
		try {
			jr = JsonResponse.getInstance(requestRepo.findByUserNotAndStatus(userRepo.findById(id).get(), "Review"));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
			e.printStackTrace();
		}
		return jr;
	}

	// Post - adds a new Request
	@PostMapping("/")
	public JsonResponse addRequest(@RequestBody Request r) {
		// add a new Request
		JsonResponse jr = null;
		try {
			r.setStatus("New");
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

	// put - submit review 
	@PutMapping("/submit-review")
	public JsonResponse submitForReview(@RequestBody Request r) {
		JsonResponse jr = null;
		// status automatically approves for items <= 50
		if (r.getTotal() <= 50) {
			r.setStatus("Approved");
		} else {
			r.setStatus("Review");
		}
		// set submitted date to local current time
		r.setSubmittedDate(LocalDateTime.now());
		try {
			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	// put - reject a request
	@PutMapping("/reject")
	public JsonResponse rejectRequest(@RequestBody Request r) {
		JsonResponse jr = null;
		// set status to rejected
		try {
			r.setStatus("Rejected");
			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	// put - approve a request
	@PutMapping("/approve")
	public JsonResponse approveRequest(@RequestBody Request r) {
		JsonResponse jr = null;
		// set status to rejected
		try {
			r.setStatus("Approved");
			jr = JsonResponse.getInstance(requestRepo.save(r));
		} catch (Exception e) {
			jr = JsonResponse.getInstance(e);
		}
		return jr;
	}

	// update - update a new Request
	@PutMapping("/")
	public JsonResponse updateRequest(@RequestBody Request r) {
		// update a request
		JsonResponse jr = null;
		r.setStatus("New");
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
