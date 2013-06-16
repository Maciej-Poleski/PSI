#include "User.h"

#include <Wt/Dbo/Impl>
#include <Wt/Auth/Dbo/AuthInfo>

#include "CardItem.hxx"
#include "Item.hxx"

void User::addItem(Wt::Dbo::ptr< Item > item, std::size_t count)
{
    Wt::Dbo::ptr<CardItem> q= _cardItems.find().where("item_id = ?").bind(item);
    if(q)
        q.modify()->count+=count;
    else
    {
        Wt::Dbo::ptr<CardItem> cardItem = session()->add(new CardItem);
        cardItem.modify()->item=item;
        cardItem.modify()->count=count;
        cardItem.modify()->user=self();
    }
    _cardChanged.emit();
}

void User::removeItem(Wt::Dbo::ptr< Item > item, std::size_t count)
{
    Wt::Dbo::ptr<CardItem> q= _cardItems.find().where("item_id = ?").bind(item);
    if(q)
    {
        q.modify()->count-=count;
        if(q->count<=0)
            q.remove();
    }
    _cardChanged.emit();
}

Wt::Signal< >& User::cardChanged() const
{
    return _cardChanged;
}

bool User::performTransaction()
{
    for(auto item : _cardItems)
    {
        if(item->count>item->item->ammount)
            return false;
    }
    for(auto item : _cardItems)
    {
        item->item.modify()->ammount-=item->count;
        item.remove();
    }
    _cardChanged.emit();
    Item::databaseChanged.emit();
    return true;
}

std::size_t User::cardItemsCount() const
{
    return _cardItems.size();
}

std::int64_t User::totalPrice() const
{
    std::int64_t result=0;
    for(auto item : _cardItems)
        result+=item->count*item->item->price;
    return result;
}

std::vector< Wt::Dbo::ptr< CardItem > > User::cardItems() const
{
    return std::vector<Wt::Dbo::ptr<CardItem>>(_cardItems.begin(),_cardItems.end());
}

DBO_INSTANTIATE_TEMPLATES(User);

