#if !defined(BANK_ACCOUNT_H)
#define BANK_ACCOUNT_H

#include <mutex>
#include <stdexcept>

namespace Bankaccount {
class Bankaccount {
public:
    Bankaccount();
    void open();
    void close();
    void deposit(int amount);
    void withdraw(int amount);
    int balance() const;

private:
    mutable std::mutex mtx;
    bool is_open;
    int current_balance;
};  // class Bankaccount

}  // namespace Bankaccount

#endif  // BANK_ACCOUNT_H
