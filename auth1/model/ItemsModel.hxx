/*
 * Copyright 2013  Maciej Poleski
 */

#ifndef ITEMSMODEL_H
#define ITEMSMODEL_H

#include <Wt/WAbstractTableModel>

#include <vector>

#include <Wt/Dbo/ptr>

class Item;
class Session;

class ItemsModel : public Wt::WAbstractTableModel
{
public:
    ItemsModel(Session &session, Wt::WObject* parent);
    virtual boost::any data(const Wt::WModelIndex& index, int role) const override;
    virtual int rowCount(const Wt::WModelIndex& parent) const override;
    virtual int columnCount(const Wt::WModelIndex& parent) const override;
    virtual boost::any headerData(int section, Wt::Orientation orientation, int role) const override;
    virtual bool insertRows(int row, int count, const Wt::WModelIndex& parent) override;
    virtual bool setData(const Wt::WModelIndex& index, const boost::any& value, int role) override;

    virtual Wt::WFlags< Wt::ItemFlag > flags(const Wt::WModelIndex& index) const override;

    virtual void sort(int column, Wt::SortOrder order = Wt::AscendingOrder) override;

    void reload();

protected:
    void reset();

private:
    Session &_session;
    std::vector<Wt::Dbo::ptr<Item>> _items;
};

#endif // ITEMSMODEL_H
