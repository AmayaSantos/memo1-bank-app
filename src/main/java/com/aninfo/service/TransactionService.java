package com.aninfo.service;

import com.aninfo.exceptions.ClassNotFoundException;
import com.aninfo.exceptions.InvalidTransactionTypeException;
import com.aninfo.model.Transaction;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.List;

@Service
public class TransactionService {

    @Autowired
    private TransactionRepository transactionRepository;

    @Autowired
    private AccountService accountService;

    @Transactional
    public Collection<Transaction> getTransactions() {
        return transactionRepository.findAll();
    }

    @Transactional
    public List<Transaction> getTransactionsByCbu(Long cbu) {
        return transactionRepository.findByAccount_Cbu(cbu);
    }

    @Transactional
    public Transaction createTransaction(Long cbu, Double sum, String type) {
        Transaction transaction = new Transaction();
        Double balanceInitial = accountService.findAccountByCbu(cbu).getBalance();

        if (type.equals("deposit")) {
            transaction.setAccount(accountService.deposit(cbu, sum));
        } else if (type.equals("extraction")) {
            transaction.setAccount(accountService.withdraw(cbu, sum));
        } else {
            throw new InvalidTransactionTypeException("... error type");
        }

        transaction.setSum(transaction.getAccount().getBalance() - balanceInitial);
        transaction.setType(type);
        transactionRepository.save(transaction);
        return transaction;
    }

    @Transactional
    public void deleteById(Long id) {
        Transaction transaction =
                transactionRepository
                        .findById(id)
                        .orElseThrow(() -> new ClassNotFoundException("Transaction not found with id " + id));

        if (transaction.getType().equals("deposit")) {
            accountService.save(
                    accountService.withdraw(transaction.getAccount().getCbu(), transaction.getSum()));
        } else if (transaction.getType().equals("extraction")) {
            accountService.save(
                    accountService.deposit(transaction.getAccount().getCbu(), transaction.getSum()));
        }
        transactionRepository.deleteById(id);
    }

    public Transaction getTransactionById(Long id) {
        return transactionRepository
                .findById(id)
                .orElseThrow(() -> new ClassNotFoundException("Transaction not found with id " + id));

    }
}
