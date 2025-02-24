#if !defined(BANK_ACCOUNT_H)
#define BANK_ACCOUNT_H

#include <mutex>
#include <stdexcept>

namespace Bankaccount {
class Bankaccount {
private:
    mutable std::mutex mtx;
    int balance{0};
    bool closed{false};

    void checkClosed() const {
        if (closed) {
            throw std::runtime_error("Account is closed");
        }
    }

public:
    void deposit(int amount);
    void withdraw(int amount);
    int balance() const;
    void close();
    void open();
    
    // Constructor/destructor
    Bankaccount() = default;
    ~Bankaccount() = default;
    
    // Prevent copying/moving
    Bankaccount(const Bankaccount&) = delete;
    Bankaccount& operator=(const Bankaccount&) = delete;
};

}  // namespace Bankaccount

#endif  // BANK_ACCOUNT_H
