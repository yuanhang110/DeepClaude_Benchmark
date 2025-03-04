#if !defined(BANK_ACCOUNT_H)
#define BANK_ACCOUNT_H

#include <mutex>

namespace Bankaccount {
class Bankaccount {
public:
    Bankaccount();
    
    void open();
    void close();
    int balance() const;
    void deposit(int amount);
    void withdraw(int amount);

private:
    mutable std::mutex mtx;
    bool is_open;
    int account_balance;
};

}  // namespace Bankaccount

#endif  // BANK_ACCOUNT_H
