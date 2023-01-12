package com.springnewexample.capitalone.SpringDemoNew;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class BookController {

	@Autowired
	BookRepo bookrepo;
	
	@GetMapping("/allbooks")	
	public ResponseEntity<List<Book>> getAllTutorials(@RequestParam(required = false) String title) {
		try {
			List<Book> books = new ArrayList<Book>();

			if (title == null)
				bookrepo.findAll().forEach(books::add);
			else
				bookrepo.findByTitleContaining(title).forEach(books::add);

			if (books.isEmpty()) {
				return new ResponseEntity<>(HttpStatus.NO_CONTENT);
			}

			return new ResponseEntity<>(books, HttpStatus.OK);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

	@Cacheable("book")
	@GetMapping("/books/{id}")
	public ResponseEntity<Book> getTutorialById(@PathVariable("id") long id) throws InterruptedException {
		Optional<Book> bookData = bookrepo.findById(id);

		Thread.sleep(100);
		if (bookData.isPresent()) {
			return new ResponseEntity<>(bookData.get(), HttpStatus.OK);
		} else {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
	}

	@PostMapping("/books")
	public ResponseEntity<Book> createTutorial(@RequestBody Book bookinput) {
		try {
			Book book= bookrepo.save(bookinput);
			return new ResponseEntity<>(book, HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	@PutMapping("/books/{id}")
	public ResponseEntity<Book> updateTutorial(@PathVariable("id") long id, @RequestBody Book bookinput) {
		try {
			Book book= bookrepo.findById(id).get();
			book.setTitle(bookinput.getTitle());
		//	book.setPublished(bookinput.getPublished());
			book.setDescription(bookinput.getDescription());
			
			return new ResponseEntity<>(bookrepo.save(book), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>(null, HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}
	
	
	
	@DeleteMapping("/books/{id}")
	ResponseEntity<String> deleteBookByID(@PathVariable("id") long id){
		bookrepo.deleteById(id);
		return ResponseEntity.ok("Book Deleted");
	}
}
