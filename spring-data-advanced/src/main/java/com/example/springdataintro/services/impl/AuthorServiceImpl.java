package com.example.springdataintro.services.impl;

import com.example.springdataintro.entities.Author;
import com.example.springdataintro.repository.AuthorRepository;
import com.example.springdataintro.services.AuthorService;
import com.example.springdataintro.utils.FileUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;

import static com.example.springdataintro.constants.GlobalConstants.AUTHOR_FILE_PATH;

@Service
public class AuthorServiceImpl implements AuthorService {
    private final AuthorRepository authorRepository;
    private FileUtil fileUtil;

    @Autowired
    public AuthorServiceImpl(AuthorRepository authorRepository, FileUtil fileUtil) {
        this.authorRepository = authorRepository;
        this.fileUtil = fileUtil;
    }


    @Override
    public void seedAuthors() throws IOException {
        if (this.authorRepository.count() != 0){
            return;
        }

        String[] authors = this.fileUtil.readFileContent(AUTHOR_FILE_PATH);
        for (String line : authors) {
            Author author = new Author();
            String[] authorFirstAndLastName = line.split("\\s+");
            author.setFirstName(authorFirstAndLastName[0]);
            author.setLastName(authorFirstAndLastName[1]);
            this.authorRepository.saveAndFlush(author);
        }
    }

    @Override
    public int getAllAuthorsCount() {
        return (int) this.authorRepository.count();
    }

    @Override
    public Author findAuthorById(long id) {
        return this.authorRepository.findById(id).orElse(null);
    }

    @Override
    public List<Author> getAllAuthorsContainsCurrentLetters(String letters) {
         return this.authorRepository.getAuthorsByFirstNameEndingWith(letters);
    }

}
