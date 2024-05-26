package com.aninfo.service;

import com.aninfo.exceptions.DepositNegativeSumException;
import com.aninfo.exceptions.InsufficientFundsException;
import com.aninfo.model.Account;
import com.aninfo.model.Transaction;
import com.aninfo.repository.AccountRepository;
import com.aninfo.repository.TransactionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.Collection;
import java.util.Optional;

@Service
public class AccountService {

    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private TransactionRepository transactionRepository;

    public Account createAccount(Account account) {
        return accountRepository.save(account);
    }

    public Collection<Account> getAccounts() {
        return accountRepository.findAll();
    }

    public Optional<Account> findById(Long cbu) {
        return accountRepository.findById(cbu);
    }

    public void save(Account account) {
        accountRepository.save(account);
    }

    public void deleteById(Long cbu) {
        accountRepository.deleteById(cbu);
    }

    @Transactional
    public Account withdraw(Long cbu, Double sum) {
        Account account = accountRepository.findAccountByCbu(cbu);

        if (account.getBalance() < sum) {
            throw new InsufficientFundsException("Insufficient funds");
        }
        Transaction  t = new Transaction(-sum, account.getCbu());

        account.setBalance(account.getBalance() - sum);
        accountRepository.save(account);
        transactionRepository.save(t);
        return account;
    }

    @Transactional
    public Account deposit(Long cbu, Double sum) {

        if (sum <= 0) {
            throw new DepositNegativeSumException("Cannot deposit negative sums");
        }

        double promo = 0;
        if (sum >=2000)
        {
            promo = sum * 0.1;
            if (promo > 500)
            {
                promo = 500;
            }
        }
        Account account = accountRepository.findAccountByCbu(cbu);
        Transaction  t = new Transaction(sum, account.getCbu());
        account.setBalance(account.getBalance() + sum + promo);
        accountRepository.save(account);
        transactionRepository.save(t);

        return account;
    }

    public Collection<Transaction> getTransactions(Long cbu) {
        return transactionRepository.findByAccountCbu(cbu);
    }

    public Optional<Transaction> getTransaction(long id) {
        return transactionRepository.findById(id);
    }
    public void deleteTransaction(long id)
    {
        Optional<Transaction> t = transactionRepository.findById(id);
        double monto = t.get().getSum();
        long cbu_origen = t.get().getAccountCbu();
        Account cuenta = accountRepository.findAccountByCbu(cbu_origen);
        cuenta.setBalance(cuenta.getBalance() - monto);
        transactionRepository.deleteById(id);

    }
}
