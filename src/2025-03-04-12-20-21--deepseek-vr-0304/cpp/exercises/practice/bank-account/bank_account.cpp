#include "bank_account.h"

namespace Bankaccount {

Bankaccount::Bankaccount() : balance_(0), is_closed(true) {}

void Bankaccount::open() {
    std::lock_guard<std::mutex> lock(mtx);
    is_closed = false;
    balance_ = 0;
}

void Bankaccount::deposit(int amount) {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_closed) {
        throw std::runtime_error("Account closed");
    }
    if (amount <= 0) {
        throw std::runtime_error("Invalid amount");
    }
    balance_ += amount;
}

void Bankaccount::withdraw(int amount) {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_closed) {
        throw std::runtime_error("Account closed");
    }
    if (amount <= 0) {
        throw std::runtime_error("Invalid amount");
    }
    if (balance_ < amount) {
        throw std::runtime_error("Insufficient funds");
    }
    balance_ -= amount;
}

int Bankaccount::balance() const {
    std::lock_guard<std::mutex> lock(mtx);
    if (is_closed) {
        throw std::runtime_error("Account closed");
    }
    return balance_;
}

void Bankaccount::close() {
    std::lock_guard<std::mutex> lock(mtx);
    is_closed = true;
}

}  // namespace Bankaccount
