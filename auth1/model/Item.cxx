#include "Item.hxx"

#include <Wt/Dbo/Impl>
#include <Wt/Auth/Dbo/AuthInfo>

#include "CardItem.hxx"
#include "User.h"

Wt::Signal<> Item::databaseChanged;

DBO_INSTANTIATE_TEMPLATES(Item);

