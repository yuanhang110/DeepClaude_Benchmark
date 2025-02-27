#include "bank_account.h"

namespace Bankaccount {

Bankaccount::Bankaccount() : is_open(false), current_balance(0) {}

void Bankaccount::open() {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_open) {
        throw std::runtime_error("Account already open");
    }
    is_open = true;
    current_balance = 0;
}

void Bankaccount::close() {
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account not open");
    }
    is_open = false;
}

void Bankaccount::deposit(int amount) {
    if (amount <= 0) {
        throw std::runtime_error("Invalid deposit amount");
    }
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account closed");
    }
    current_balance += amount;
}

void Bankaccount::withdraw(int amount) {
    if (amount <= 0) {
        throw std::runtime_error("Invalid withdrawal amount");
    }
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account closed");
    }
    if (current_balance < amount) {
        throw std::runtime_error("Insufficient funds");
    }
    current_balance -= amount;
}

int Bankaccount::balance() const {
    std::lock_guard<std::mutex> lock(mtx);
    if (!is_open) {
        throw std::runtime_error("Account closed");
    }
    return current_balance;
}

}  // namespace Bankaccount
