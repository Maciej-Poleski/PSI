/*
 * Copyright 2013  Maciej Poleski
 */

#include "CardItem.hxx"

#include "Item.hxx"

std::string CardItem::name() const
{
    return item->name;
}

int64_t CardItem::costPerItem() const
{
    return item->price;
}

int64_t CardItem::totalPrice() const
{
    return count*costPerItem();
}

bool CardItem::acceptable() const
{
    return count<=item->ammount;
}

#include <Wt/Dbo/collection_impl.h>
#include <Wt/Dbo/ptr>

template Wt::Dbo::collection<Wt::Dbo::ptr<CardItem> >::collection();
