/*
 * Copyright 2013  Maciej Poleski
 */

#ifndef CARDITEM_H
#define CARDITEM_H

#include <Wt/Dbo/ptr>
#include <Wt/Dbo/Field>

class User;
class Item;

class CardItem
{
public:
    Wt::Dbo::ptr<Item> item;
    std::int64_t count;
    Wt::Dbo::ptr<User> user;

    std::string name() const;
    std::int64_t costPerItem() const;
    std::int64_t totalPrice() const;

    template<class Action>
    void persist(Action &a)
    {
        Wt::Dbo::belongsTo(a,item,"item");
        Wt::Dbo::field(a,count,"count");
        Wt::Dbo::belongsTo(a,user,"user");
    }
};

#endif // CARDITEM_H
