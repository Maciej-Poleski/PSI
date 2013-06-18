/*
 * Copyright 2013  Maciej Poleski
 */

#ifndef NAMEITEMDELEGATE_H
#define NAMEITEMDELEGATE_H

#include <Wt/WItemDelegate>

class NameItemDelegate : public Wt::WItemDelegate
{
public:
    virtual Wt::WValidator::State validate(const Wt::WModelIndex& index, const boost::any& editState) const override;
};

#endif // NAMEITEMDELEGATE_H
