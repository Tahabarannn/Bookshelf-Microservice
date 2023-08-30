package com.bookshelf.bookservice.dto

import com.bookshelf.bookservice.model.Book

data class BookDto @JvmOverloads constructor(
        val id: BookIdDto? = null,
        val title: String,
        val bookYear: Int,
        val author: String,
        val pressName: String,
        val isbn: String,
) {
companion object {
        @JvmStatic
        fun convert(from: Book): BookDto {
            return BookDto(
                    BookIdDto.convert(from.id, from.isbn),
                    from.title,
                    from.bookYear,
                    from.author,
                    from.pressName,
                    from.isbn
            )
        }
    }
}