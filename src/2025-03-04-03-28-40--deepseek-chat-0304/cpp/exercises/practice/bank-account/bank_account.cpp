#include "bank_account.h"
#include <stdexcept>

namespace Bankaccount {

Bankaccount::Bankaccount() : is_open(false), account_balance(0) {}

void Bankaccount::open() {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_open) {
        throw std::runtime_error("Account is already open");
    }
    is_open = true;
    account_balance = 0;
}

void Bankaccount::close() {
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account is already closed");
    }
    is_open = false;
}

int Bankaccount::balance() const {
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account is closed");
    }
    return account_balance;
}

void Bankaccount::deposit(int amount) {
    if (amount <= 0) {
        throw std::runtime_error("Deposit amount must be positive");
    }
    
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account is closed");
    }
    account_balance += amount;
}

void Bankaccount::withdraw(int amount) {
    if (amount <= 0) {
        throw std::runtime_error("Withdrawal amount must be positive");
    }
    
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account is closed");
    }
    if (amount > account_balance) {
        throw std::runtime_error("Insufficient funds");
    }
    account_balance -= amount;
}

}  // namespace Bankaccount
