package com.example.LMS_Backend.restcontroller;

import com.example.LMS_Backend.common.Constants;
import com.example.LMS_Backend.model.Book;
import com.example.LMS_Backend.model.Issue;
import com.example.LMS_Backend.model.IssuedBook;
import com.example.LMS_Backend.model.Member;
import com.example.LMS_Backend.service.BookService;
import com.example.LMS_Backend.service.IssueService;
import com.example.LMS_Backend.service.IssuedBookService;
import com.example.LMS_Backend.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping(value="/rest/issue")
public class IssueRestController {

    @Autowired
    private MemberService memberService;

    @Autowired
    private BookService bookService;

    @Autowired
    private IssueService issueService;

    @Autowired
    private IssuedBookService issuedBookService;

    @RequestMapping(value="/save", method = RequestMethod.POST)
    public String save(@RequestParam Map<String, String > payload){
        String memberIdStr = (String)payload.get("member");
        String[] bookIdStr = payload.get("books").toString().split(",");

        Long memberId = null;
        List<Long> booksIds = new ArrayList<Long>();
        try{
            memberId = Long.parseLong(memberIdStr);
            for(int k=0; k<bookIdStr.length; k++){
                booksIds.add(Long.parseLong(bookIdStr[k]));
            }
        }
        catch(NumberFormatException ex){
            ex.printStackTrace();
            return "invalid number format";
        }

        Member member = memberService.get(memberId);
        List<Book> books = bookService.get(booksIds);

       Issue issue = new Issue();
       Issue.setMember(member);
       issue = issueService.addNew(issue);

       for( int k=0; k<books.size();k++){
           Book book = books.get(k);
           book.setStatus(Constants.BOOK_STATUS_ISSUED);
           book = bookService.save(book);

           IssuedBook ib = new IssuedBook();
           ib.setBook(book);
           ib.setIssue(issue);
           issuedBookService.addNew(ib);
       }
       return "success";
    }

    @RequestMapping(value="/{id}/return/all", method = RequestMethod.GET)
    public String returnAll(@PathVariable(name="id")Long id){
        Issue issue = issueService.get(id);
        if( issue != null){
            List<IssuedBook> issuedBooks = issue.getIssuedBooks();
            for(int k=0 ; k<issuedBooks.size();k++){
                IssuedBook ib = issuedBooks.get(k);
                ib.setReturned(Constants.BOOK_RETURNED);
                issuedBookService.save(ib);

                Book book = ib.getBook();
                book.setStatus(Constants.BOOK_STATUS_AVAILABLE);
                bookService.save(book);
            }

            issue.setReturned(Constants.BOOK_RETURNED);
            issueService.save(issue);

            return "successful";
        }
        else{
            return "unsuccessful";
        }
    }

    @RequestMapping(value="/{id}/return", method = RequestMethod.POST)
    public String returnSelected(@RequestParam Map<String, String> payload, @PathVariable(name="id")Long id){
        Issue issue = issueService.get(id);
        String[] issuedBookIds = payload.get("ids").split(",");
        if( issue != null){
            List<IssuedBook> issuedBooks = issue.getIssuedBooks();
            for(int k=0; k<issuedBooks.size();k++){
                IssuedBook ib = issuedBooks.get(k);
                if(Arrays.asList(issuedBookIds).contains(ib.getId().toString())){
                    ib.setReturned(Constants.BOOK_RETURNED);
                    issuedBookService.save(ib);

                    Book book = ib.getBook();
                    book.setStatus(Constants.BOOK_STATUS_AVAILABLE);
                    bookService.save(book);
                }
            }

            return "successful";
        }
        else{
            return "unsuccessful";
        }
    }
}
