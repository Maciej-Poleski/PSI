#ifndef ITEM_H_
#define ITEM_H_

#include <Wt/Dbo/Types>
#include <Wt/WGlobal>
#include <Wt/WSignal>

class CardItem;
namespace dbo = Wt::Dbo;

class Item {
public:
    std::string name;
    std::int64_t ammount;
    std::int64_t price;

    dbo::collection<dbo::ptr<CardItem>> items;

    template<class Action>
    void persist(Action& a)
    {
        dbo::field(a,name,"name");
        dbo::field(a,ammount,"ammount");
        dbo::field(a,price,"price");
        dbo::hasMany(a,items,Wt::Dbo::ManyToOne,"item");
    }

    static Wt::Signal<> databaseChanged;
};


DBO_EXTERN_TEMPLATES(Item);

#endif // ITEM_H_
