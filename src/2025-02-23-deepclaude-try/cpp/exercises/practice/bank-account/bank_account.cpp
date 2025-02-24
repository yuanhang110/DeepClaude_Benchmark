#include "bank_account.h"

namespace Bankaccount {

Bankaccount::Bankaccount() : closed(true), balance(0.0) {}

bool Bankaccount::close() {
    std::lock_guard<std::mutex> lock(mtx);
    if (closed) {
        return false; // already closed
    }
    closed = true;
    return true;
}

bool Bankaccount::open() {
    std::lock_guard<std::mutex> lock(mtx);
    if (!closed) {
        throw std::runtime_error("Cannot open already opened account");
    }
    closed = false;
    balance = 0.0;
    return true;
}

bool Bankaccount::is_closed() const {
    std::lock_guard<std::mutex> lock(mtx);
    return closed;
}

bool Bankaccount::deposit(double amount) {
    if (amount <= 0) {
        return false;
    }
    std::lock_guard<std::mutex> lock(mtx);
    if (closed) {
        return false;
    }
    balance += amount;
    return true;
}

bool Bankaccount::withdraw(double amount) {
    if (amount <= 0) {
        return false;
    }
    std::lock_guard<std::mutex> lock(mtx);
    if (closed || balance < amount) {
        return false;
    }
    balance -= amount;
    return true;
}

double Bankaccount::balance() const {
    std::lock_guard<std::mutex> lock(mtx);
    if (closed) {
        throw std::runtime_error("Account closed");
    }
    return balance;
}

} // namespace Bankaccount
