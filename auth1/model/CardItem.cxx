/*
 * Copyright 2013  Maciej Poleski
 */

#include "CardItem.hxx"

#include "Item.hxx"
#include "User.h"

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
