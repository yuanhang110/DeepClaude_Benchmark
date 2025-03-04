#if !defined(BANK_ACCOUNT_H)
#define BANK_ACCOUNT_H

#include <mutex>
#include <stdexcept>

namespace Bankaccount {
class Bankaccount {
public:
    Bankaccount();
    void open();
    void deposit(int amount);
    void withdraw(int amount);
    int balance() const;
    void close();

private:
    mutable std::mutex mtx;
    int balance_;
    bool is_closed;
};  // class Bankaccount

}  // namespace Bankaccount

#endif  // BANK_ACCOUNT_H
