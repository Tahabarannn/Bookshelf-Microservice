package com.bookshelf.libraryservice.client;

import com.bookshelf.libraryservice.dto.BookDto;
import com.bookshelf.libraryservice.dto.BookIdDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "book-service", path = "/v1/books")
public interface BookServiceClient {

    @GetMapping("/isbn/{isbn}")
    ResponseEntity<BookIdDto> getBookByIsbn(@PathVariable String isbn);

    @GetMapping("/book/{id}")
    ResponseEntity<BookDto> getBookById(@PathVariable String id);
}
