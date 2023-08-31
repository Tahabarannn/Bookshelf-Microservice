package com.bookshelf.libraryservice.service;

import com.bookshelf.libraryservice.dto.LibraryDto;
import com.bookshelf.libraryservice.exception.LibraryNotFoundException;
import com.bookshelf.libraryservice.model.Library;
import com.bookshelf.libraryservice.repository.LibraryRepository;
import org.springframework.stereotype.Service;

@Service
public class LibraryService {

    private final LibraryRepository libraryRepository;

    public LibraryService(LibraryRepository libraryRepository) {
        this.libraryRepository = libraryRepository;
    }

    public LibraryDto getAllBooksInLibraryById(String id) {
        Library library = libraryRepository.findById(id)
                .orElseThrow(() -> new LibraryNotFoundException("Library not found with id: " + id));

        LibraryDto libraryDto = new LibraryDto(library.getId());
        return libraryDto;
    }

    public LibraryDto createLibrary() {
        Library newLibrary = libraryRepository.save(new Library());
        return new LibraryDto(newLibrary.getId());
    }


}
