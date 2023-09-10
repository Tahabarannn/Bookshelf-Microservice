package com.bookshelf.libraryservice.service;

import com.bookshelf.BookService.BookId;
import com.bookshelf.BookService.BookServiceGrpc;
import com.bookshelf.BookService.Isbn;
import com.bookshelf.libraryservice.client.BookServiceClient;
import com.bookshelf.libraryservice.dto.AddBookRequest;
import com.bookshelf.libraryservice.dto.LibraryDto;
import com.bookshelf.libraryservice.exception.LibraryNotFoundException;
import com.bookshelf.libraryservice.model.Library;
import com.bookshelf.libraryservice.repository.LibraryRepository;
import net.devh.boot.grpc.client.inject.GrpcClient;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;
    private final BookServiceClient bookServiceClient;

    @GrpcClient("book-service")
    private BookServiceGrpc.BookServiceBlockingStub bookServiceBlockingStub;

    public LibraryService(LibraryRepository libraryRepository, BookServiceClient bookServiceClient) {
        this.libraryRepository = libraryRepository;
        this.bookServiceClient = bookServiceClient;
    }

    public LibraryDto getAllBooksInLibraryById(String id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with id: " + id));

        LibraryDto libraryDto = new LibraryDto(library.getId(),
                library.getUserBook()
                        .stream()
                        .map(book -> bookServiceClient.getBookById(book).getBody())
                        .collect(Collectors.toList()));
        return libraryDto;
    }

    public LibraryDto createLibrary() {
        Library newLibrary = libraryRepository.save(new Library());
        return new LibraryDto(newLibrary.getId());
    }

    public void addBookToLibrary(AddBookRequest request) {
        BookId bookId = bookServiceBlockingStub.getBookIdByIsbn(Isbn.newBuilder().setIsbn(request.getIsbn()).build());

        Library library = libraryRepository.findById(request.getId())
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with id: " + request.getId()));

        library.getUserBook().add(bookId.getBookId());

        libraryRepository.save(library);
    }

    public List<String> getAllLibraries() {
        return libraryRepository.findAll()
                .stream()
                .map(Library::getId)
                .collect(Collectors.toList());
    }


}
