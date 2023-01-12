package com.springnewexample.capitalone.SpringDemoNew;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface BookRepo extends JpaRepository<Book, Long>{

	  List<Book> findByPublished(boolean published);

	  List<Book> findByTitleContaining(String title);
}