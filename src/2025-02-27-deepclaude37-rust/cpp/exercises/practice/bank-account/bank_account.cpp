#include "bank_account.h"
#include <stdexcept>

namespace Bankaccount {

Bankaccount::Bankaccount() : balance(0.0), is_closed(true) {}

void Bankaccount::open() {
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_closed) {
        throw std::runtime_error("Account already open");
    }
    is_closed = false;
    balance = 0.0;
}

void Bankaccount::deposit(double amount) {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_closed) {
        throw std::runtime_error("Account is closed");
    }
    if (amount <= 0) {
        throw std::runtime_error("Invalid deposit amount");
    }
    balance += amount;
}

void Bankaccount::withdraw(double amount) {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_closed) {
        throw std::runtime_error("Account is closed");
    }
    if (amount <= 0) {
        throw std::runtime_error("Invalid withdrawal amount");
    }
    if (amount > balance) {
        throw std::runtime_error("Insufficient funds");
    }
    balance -= amount;
}

void Bankaccount::close() {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_closed) {
        throw std::runtime_error("Account already closed");
    }
    is_closed = true;
}

double Bankaccount::balance() const {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_closed) {
        throw std::runtime_error("Account is closed");
    }
    return balance;
}

}  // namespace Bankaccount
