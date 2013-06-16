/*
 * Copyright 2013  Maciej Poleski <maciej.poleski@uj.edu.pl>
 */

#include "BuyModel.hxx"

#include <boost/any.hpp>

#include <Wt/WCssDecorationStyle>

#include "Session.h"
#include "CardItem.hxx"

BuyModel::BuyModel(Session& session, Wt::WObject* parent): WAbstractTableModel(parent), _session(session)
{
    userChanged();
}


boost::any BuyModel::data(const Wt::WModelIndex& index, int role) const
{
    if(_session.user())
    {
        if(role==Wt::DisplayRole)
        {
            if(index.column()==0)
            {
                return _cardItems[index.row()]->name();
            }
            else if(index.column()==1)
            {
                return _cardItems[index.row()]->costPerItem();
            }
            else if(index.column()==2)
            {
                return _cardItems[index.row()]->count;
            }
            else if(index.column()==3)
            {
                return _cardItems[index.row()]->totalPrice();
            }
        }
        else if(role==Wt::StyleClassRole)
        {
            if(!_cardItems[index.row()]->acceptable())
                return "red_background";
        }
    }
    return boost::any();
}

int BuyModel::rowCount(const Wt::WModelIndex& parent) const
{
    return _cardItems.size();
}

int BuyModel::columnCount(const Wt::WModelIndex& parent) const
{
    return 4;
}

void BuyModel::reload()
{
    reset();
}

void BuyModel::userChanged()
{
    reset();
}

void BuyModel::reset()
{
    Wt::Dbo::Transaction t(_session);
    if(_session.user())
    {
        _cardItems=_session.user()->cardItems();
    }
    else
    {
        _cardItems.clear();
    }
    WAbstractTableModel::reset();
}

boost::any BuyModel::headerData(int section, Wt::Orientation orientation, int role) const
{
    if(orientation==Wt::Horizontal)
    {
        if(role==Wt::DisplayRole)
        {
            if(section==0)
            {
                return Wt::WString::fromUTF8("Nazwa");
            }
            else if(section==1)
            {
                return Wt::WString::fromUTF8("Cena za sztuke");
            }
            else if(section==2)
            {
                return Wt::WString::fromUTF8("Sztuk");
            }
            else if(section==3)
            {
                return Wt::WString::fromUTF8("Koszt");
            }
        }
    }
    return Wt::WAbstractItemModel::headerData(section, orientation, role);
}
