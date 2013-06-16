/*
 * Copyright 2013  Maciej Poleski <maciej.poleski@uj.edu.pl>
 */

#ifndef BUYMODEL_H
#define BUYMODEL_H

#include <Wt/WAbstractTableModel>

#include <Wt/Dbo/ptr>

class CardItem;
class Session;

class BuyModel : public Wt::WAbstractTableModel
{
public:
    BuyModel(Session &session, WObject* parent = 0);

    virtual boost::any data(const Wt::WModelIndex& index, int role) const override;
    virtual int rowCount(const Wt::WModelIndex& parent) const override;
    virtual int columnCount(const Wt::WModelIndex& parent) const override;

    void reload();
    void userChanged();

protected:
    void reset();

private:
    Session &_session;
    std::vector<Wt::Dbo::ptr<CardItem>> _cardItems;
};

#endif // BUYMODEL_H
