#if !defined(BANK_ACCOUNT_H)
#define BANK_ACCOUNT_H

#include <mutex>
#include <stdexcept>

namespace Bankaccount {
class Bankaccount {
public:
    explicit Bankaccount();
    
    void open();
    void deposit(double amount);
    void withdraw(double amount);
    void close();
    double balance() const;
    
    // Prevent copying to avoid mutex issues
    Bankaccount(const Bankaccount&) = delete;
    Bankaccount& operator=(const Bankaccount&) = delete;

private:
    mutable std::mutex mtx;  // mutable so it can be locked in const methods
    double balance;
    bool is_closed;
};  // class Bankaccount

}  // namespace Bankaccount

#endif  // BANK_ACCOUNT_H
