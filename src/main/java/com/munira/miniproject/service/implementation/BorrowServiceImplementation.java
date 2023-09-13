package com.munira.miniproject.service.implementation;

import com.munira.miniproject.entity.BookEntity;
import com.munira.miniproject.entity.BorrowEntity;
import com.munira.miniproject.entity.ReserveEntity;
import com.munira.miniproject.entity.UserEntity;
import com.munira.miniproject.exception.*;
import com.munira.miniproject.model.BorrowDto;
import com.munira.miniproject.model.BorrowInfoDto;
import com.munira.miniproject.repository.BookRepository;
import com.munira.miniproject.repository.BorrowRepository;
import com.munira.miniproject.repository.ReserveRepository;
import com.munira.miniproject.repository.UserRepository;
import com.munira.miniproject.service.BorrowService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@Transactional
public class BorrowServiceImplementation implements BorrowService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BorrowRepository borrowRepository;

    @Autowired
    private ReserveRepository reserveRepository;

    public BorrowDto bookBorrowing(Long bookId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        if(bookEntity == null || bookEntity.isDeleted())
            throw new BookNotFoundException("Book with ID " + bookId + " is not available");

        if (Objects.equals(bookEntity.getBookStatus(), "BORROWED"))
            throw new CanNotBorrowException("Not available to borrow, you can reserve it.");

        ModelMapper modelMapper = new ModelMapper();
        BorrowEntity borrowEntity = new BorrowEntity();
        borrowEntity.setBookEntity(bookEntity);
        borrowEntity.setUserEntity(userEntity);
        borrowEntity.setBorrowDate(LocalDate.now());
        borrowEntity.setDueDate(LocalDate.now().plusDays(7));
        bookEntity.setBookStatus("BORROWED");

        BorrowEntity storeBorrowDetails = borrowRepository.save(borrowEntity);
        return modelMapper.map(storeBorrowDetails, BorrowDto.class);

    }

    public BorrowDto bookReturning(Long bookId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        Long userId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        BookEntity bookEntity = bookRepository.findByBookId(bookId);

        if(bookEntity == null || bookEntity.isDeleted() )
            throw new BookNotFoundException("Book does not exist!!");

        BorrowEntity borrowEntity = borrowRepository.findByUserEntityAndBookEntityAndReturnDateIsNull(userEntity,bookEntity);

        if(borrowEntity == null)
            throw new BookWasNotBorrowedException("This book is not borrowed by you");

        ModelMapper modelMapper = new ModelMapper();
        borrowEntity.setReturnDate(LocalDate.now());
        bookEntity.setBookStatus("AVAILABLE");

        List<ReserveEntity> reserveEntities = reserveRepository.findAllByBookEntityAndReserveStatus(bookEntity,"PENDING");
        if(!reserveEntities.isEmpty()) {
            for (ReserveEntity reserveEntity : reserveEntities) {
                reserveEntity.setReserveStatus("DONE");
            }
        }

        BorrowEntity storeReturnDetails = borrowRepository.save(borrowEntity);

        return modelMapper.map(storeReturnDetails, BorrowDto.class);
    }


    public List<BookEntity> getAllBookByUser(Long userId) throws Exception{
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();

        if (!currentUserId.equals(userId) && currentUserRole.equals("USER"))
            throw new UnauthorizedException("You are not authorized to access this!");

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)
            throw new UserNotFoundException("User id is invalid.");

        List<BorrowEntity> bookBorrowings = borrowRepository.findAllByUserEntity(userEntity);
        if(bookBorrowings.isEmpty())
            throw new BookWasNotBorrowedException("No book was borrowed by this user!");

        return bookBorrowings.stream()
                .map(BorrowEntity::getBookEntity)
                .collect(Collectors.toList());
    }

    public List<BookEntity> getAllBorrowedBookByUser(Long userId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();

        if (!currentUserId.equals(userId) && currentUserRole.equals("USER")) {
            throw new UnauthorizedException("You are not authorized to access this!");
        }

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)
            throw new UserNotFoundException("User id is invalid.");

        List<BorrowEntity> bookBorrowings = borrowRepository.findAllByUserEntityAndReturnDateIsNull(userEntity);
        if(bookBorrowings.isEmpty())
            throw new BookWasNotBorrowedException("No book was borrowed by this user!");

        return bookBorrowings.stream()
                .map(BorrowEntity::getBookEntity)
                .collect(Collectors.toList());
    }

    public List<BorrowInfoDto> getUserAllHistory(Long userId) throws Exception {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Optional<UserEntity> user = userRepository.findByEmail(authentication.getName());
        String currentUserRole = user.get().getRole();
        Long currentUserId = user.get().getUserId();

        UserEntity userEntity = userRepository.findByUserId(userId);
        if(userEntity == null)
            throw new UserNotFoundException("User id is invalid.");

        if (!currentUserId.equals(userId) && currentUserRole.equals("USER"))
            throw new UnauthorizedException("You are not authorized to access this!");

        List<BorrowEntity> bookBorrowings = borrowRepository.findAllByUserEntity(userEntity);
        if(bookBorrowings.isEmpty())
            throw new BookWasNotBorrowedException("No book was borrowed by this user!");

        return bookBorrowings.stream()
                .map(bookBorrowingEntity -> BorrowInfoDto.builder()
                        .borrowId(bookBorrowingEntity.getBorrowId())
                        .bookEntity(bookBorrowingEntity.getBookEntity())
                        .borrowDate(bookBorrowingEntity.getBorrowDate())
                        .dueDate(bookBorrowingEntity.getDueDate())
                        .returnDate(bookBorrowingEntity.getReturnDate())
                        .build()
                )
                .collect(Collectors.toList());
    }
}
