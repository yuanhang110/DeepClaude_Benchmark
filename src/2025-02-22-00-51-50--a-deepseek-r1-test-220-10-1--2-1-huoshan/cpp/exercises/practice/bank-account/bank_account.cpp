#include "bank_account.h"

namespace Bankaccount {

void Bankaccount::deposit(int amount) {
    std::lock_guard<std::mutex> lock(mtx);
    checkClosed();
    
    if (amount <= 0) {
        throw std::runtime_error("Invalid deposit amount");
    }
    balance += amount;
}

void Bankaccount::withdraw(int amount) {
    std::lock_guard<std::mutex> lock(mtx);
    checkClosed();
    
    if (amount <= 0) {
        throw std::runtime_error("Invalid withdrawal amount");
    }
    if (amount > balance) {
        throw std::runtime_error("Insufficient funds");
    }
    balance -= amount;
}

int Bankaccount::balance() const {
    std::lock_guard<std::mutex> lock(mtx);
    return balance;
}

void Bankaccount::close() {
    std::lock_guard<std::mutex> lock(mtx);
    if (closed) return;
    closed = true;
    balance = 0;
}

void Bankaccount::open() {
    std::lock_guard<std::mutex> lock(mtx);
    if (closed) {
        closed = false;
    }
}

}  // namespace Bankaccount
