#include "User.h"

#include <Wt/Dbo/Impl>
#include <Wt/Auth/Dbo/AuthInfo>

#include "CardItem.hxx"
#include "Item.hxx"

void User::addItem(Wt::Dbo::ptr< Item > item, std::size_t count)
{
    Wt::Dbo::ptr<CardItem> q= cardItems.find().where("item_id = ?").bind(item);
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
    Wt::Dbo::ptr<CardItem> q= cardItems.find().where("item_id = ?").bind(item);
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


DBO_INSTANTIATE_TEMPLATES(User);

