/*
 * Copyright 2013  Maciej Poleski
 */

#include "ItemsModel.hxx"

#include <Wt/Dbo/collection>
#include <Wt/Dbo/ptr>

#include "Session.h"
#include "CardItem.hxx"
#include "Item.hxx"

ItemsModel::ItemsModel(Session &session, Wt::WObject* parent): WAbstractTableModel(parent), _session(session)
{
    reload();
    Item::databaseChanged.connect(this,&ItemsModel::reload);
}

int ItemsModel::columnCount(const Wt::WModelIndex& parent) const
{
    return 3;
}

void ItemsModel::reload()
{
    reset();
}

void ItemsModel::reset()
{
    Wt::Dbo::Transaction t(_session);
    auto i=_session.find<Item>().resultList();
    _items.assign(i.begin(),i.end());
    WAbstractTableModel::reset();
}

boost::any ItemsModel::data(const Wt::WModelIndex& index, int role) const
{
    if(role==Wt::DisplayRole)
    {
        if(index.column()==0)
        {
            return _items[index.row()]->name;
        }
        else if(index.column()==1)
        {
            return _items[index.row()]->ammount;
        }
        else if(index.column()==2)
        {
            return _items[index.row()]->price;
        }
    }
    else if(role==Wt::EditRole)
    {
        if(index.column()==0)
        {
            return _items[index.row()]->name;
        }
        else if(index.column()==1)
        {
            return _items[index.row()]->ammount;
        }
        else if(index.column()==2)
        {
            return _items[index.row()]->price;
        }
    }

    return boost::any();
}

boost::any ItemsModel::headerData(int section, Wt::Orientation orientation, int role) const
{
    if(orientation==Wt::Horizontal)
    {
        if(role==Wt::DisplayRole)
        {
            if(section==0)
                return Wt::WString::fromUTF8("Nazwa");
            else if(section==1)
                return Wt::WString::fromUTF8("Dostępność");
            else if(section==2)
                return Wt::WString::fromUTF8("Cena");
        }
    }
    return Wt::WAbstractItemModel::headerData(section, orientation, role);
}

bool ItemsModel::insertRows(int row, int count, const Wt::WModelIndex& parent)
{
    if(row==rowCount(parent))
    {
        if(count>0)
        {
            int begin=rowCount(parent);
            int end=begin+count;
            beginInsertRows(parent,begin,end-1);
            while(count--)
            {
                auto row=_session.add(new Item {"",0,0});
                _items.push_back(row);
            }
            endInsertRows();
            Item::databaseChanged.emit();
            return true;
        }
    }
    return Wt::WAbstractItemModel::insertRows(row, count, parent);
}

int ItemsModel::rowCount(const Wt::WModelIndex& parent) const
{
    return _items.size();
}

bool ItemsModel::setData(const Wt::WModelIndex& index, const boost::any& value, int role)
{
    if(role==Wt::EditRole)
    {
        if(index.row()<rowCount(index.parent()))
        {
            if(index.column()==0)
            {
                std::string newName=boost::any_cast<Wt::WString>(value).toUTF8();
                if(newName.empty())
                    return false;
                _items[index.row()].modify()->name=newName;
                dataChanged().emit(index,index);
                Item::databaseChanged.emit();
                return true;
            }
            else if(index.column()==1)
            {
                std::string newAmmount=boost::any_cast<Wt::WString>(value).toUTF8();
                std::int64_t newAmmountInt;
                try
                {
                    newAmmountInt=boost::lexical_cast<std::int64_t>(newAmmount);
                }
                catch(boost::bad_lexical_cast)
                {
                    return false;
                }
                if(newAmmountInt<0)
                    return false;
                _items[index.row()].modify()->ammount=newAmmountInt;
                dataChanged().emit(index,index);
                Item::databaseChanged.emit();
                return true;
            }
            else if(index.column()==2)
            {
                std::string newPrice=boost::any_cast<Wt::WString>(value).toUTF8();
                std::int64_t newPriceInt;
                try
                {
                    newPriceInt=boost::lexical_cast<std::int64_t>(newPrice);
                }
                catch(boost::bad_lexical_cast)
                {
                    return false;
                }
                if(newPriceInt<=0)
                    return false;
                _items[index.row()].modify()->price=newPriceInt;
                dataChanged().emit(index,index);
                Item::databaseChanged.emit();
                return true;
            }
        }
    }
    return Wt::WAbstractItemModel::setData(index, value, role);
}

Wt::WFlags< Wt::ItemFlag > ItemsModel::flags(const Wt::WModelIndex& index) const
{
    return Wt::ItemIsEditable;
}

void ItemsModel::sort(int column, Wt::SortOrder order)
{
    if(column>=0 && column <3)
    {
        layoutAboutToBeChanged().emit();
        if(column==0)
        {
            if(order==Wt::AscendingOrder)
            {
                std::stable_sort(_items.begin(),_items.end(),
                                 [](const Wt::Dbo::ptr<Item> &lhs,
                const Wt::Dbo::ptr<Item> &rhs) {
                    return lhs->name<rhs->name;
                });
            }
            else if(order==Wt::DescendingOrder)
            {
                std::stable_sort(_items.begin(),_items.end(),
                                 [](const Wt::Dbo::ptr<Item> &lhs,
                                    const Wt::Dbo::ptr<Item> &rhs) {
                    return lhs->name>rhs->name;
                });
            }
        }
        else if(column==1)
        {
            if(order==Wt::AscendingOrder)
            {
                std::stable_sort(_items.begin(),_items.end(),
                                 [](const Wt::Dbo::ptr<Item> &lhs,
                                    const Wt::Dbo::ptr<Item> &rhs) {
                                     return lhs->ammount<rhs->ammount;
                                    });
            }
            else if(order==Wt::DescendingOrder)
            {
                std::stable_sort(_items.begin(),_items.end(),
                                 [](const Wt::Dbo::ptr<Item> &lhs,
                                    const Wt::Dbo::ptr<Item> &rhs) {
                                     return lhs->ammount>rhs->ammount;
                                    });
            }
        }
        else if(column==2)
        {
            if(order==Wt::AscendingOrder)
            {
                std::stable_sort(_items.begin(),_items.end(),
                                 [](const Wt::Dbo::ptr<Item> &lhs,
                                    const Wt::Dbo::ptr<Item> &rhs) {
                                     return lhs->price<rhs->price;
                                    });
            }
            else if(order==Wt::DescendingOrder)
            {
                std::stable_sort(_items.begin(),_items.end(),
                                 [](const Wt::Dbo::ptr<Item> &lhs,
                                    const Wt::Dbo::ptr<Item> &rhs) {
                                     return lhs->price>rhs->price;
                                    });
            }
        }
        layoutChanged().emit();
    }
}

