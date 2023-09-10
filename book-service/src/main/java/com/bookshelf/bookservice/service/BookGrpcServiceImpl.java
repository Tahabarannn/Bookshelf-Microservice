package com.bookshelf.bookservice.service;

import com.bookshelf.BookService.BookId;
import com.bookshelf.BookService.BookServiceGrpc;
import com.bookshelf.BookService.Isbn;
import com.bookshelf.bookservice.dto.BookIdDto;
import com.bookshelf.bookservice.exception.BookNotFoundExcepiton;
import com.bookshelf.bookservice.repository.BookRepository;
import io.grpc.stub.StreamObserver;
import net.devh.boot.grpc.server.service.GrpcService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@GrpcService
public class BookGrpcServiceImpl extends BookServiceGrpc.BookServiceImplBase {

    private static final Logger logger = LoggerFactory.getLogger(BookGrpcServiceImpl.class);
    private final BookRepository bookRepository;

    public BookGrpcServiceImpl(BookRepository bookRepository) {
        this.bookRepository = bookRepository;
    }

    @Override
    public void getBookIdByIsbn(Isbn request, StreamObserver<BookId> responseObserver) {
        logger.info("Grpc call received: " + request.getIsbn());
        BookIdDto bookId = bookRepository.getBookByIsbn(request.getIsbn())
                .map(book -> new BookIdDto(book.getId(), book.getIsbn()))
                .orElseThrow(() -> new BookNotFoundExcepiton("Book could not found by isbn: " + request.getIsbn()));
        responseObserver.onNext(
                BookId.newBuilder()
                        .setBookId(bookId.getBookId())
                        .setIsbn(bookId.getIsbn())
                        .build());
        responseObserver.onCompleted();
    }
}
