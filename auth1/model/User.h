#ifndef USER_H_
#define USER_H_

#include <Wt/Dbo/Types>
#include <Wt/WGlobal>
#include <Wt/WSignal>

class CardItem;
class Item;
namespace dbo = Wt::Dbo;

class User;
typedef Wt::Auth::Dbo::AuthInfo<User> AuthInfo;

class User : public Wt::Dbo::Dbo<User> {
public:

    void addItem(Wt::Dbo::ptr< Item > item, std::size_t count = 1);
    void removeItem(Wt::Dbo::ptr< Item > item, std::size_t count = 1);

    template<class Action>
    void persist(Action& a)
    {
        dbo::field(a,_isAdministrator,"is_administrator");
        dbo::hasMany(a,_cardItems,Wt::Dbo::ManyToOne,"user");
    }

    Wt::Signal<> & cardChanged() const;

    bool performTransaction();

    std::size_t cardItemsCount() const;
    std::int64_t totalPrice() const;

    std::vector<dbo::ptr<CardItem>> cardItems() const;
    
    bool _isAdministrator=false;

private:
    dbo::collection<dbo::ptr<CardItem>> _cardItems;

    mutable Wt::Signal<> _cardChanged;
};


DBO_EXTERN_TEMPLATES(User);

#endif // USER_H_
