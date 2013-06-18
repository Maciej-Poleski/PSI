cmake_minimum_required(VERSION 2.8 FATAL_ERROR)
project(server)

set(CMAKE_CXX_FLAGS "-std=c++0x")

find_package(Wt REQUIRED)
find_package(Boost REQUIRED signals system)
INCLUDE_DIRECTORIES( ${Wt_INCLUDE_DIR} )
include_directories(${Boost_INCLUDE_DIR})

add_executable(server
    model/AdministrationWidget.cxx
    model/ItemsModel.cxx
    model/BuyModel.cxx
    model/BuyWidget.cxx
    model/CardWidget.cxx
    model/CardItem.cxx
    model/Item.cxx 
    model/OfferWidget.cxx
    main.cxx
    model/Session.C
    model/User.C)

TARGET_LINK_LIBRARIES( server
    ${Wt_DEBUG_LIBRARY}        # or {Wt_LIBRARY}
    ${Wt_HTTP_DEBUG_LIBRARY}   # or {Wt_HTTP_LIBRARY}
    ${Boost_LIBRARIES}
    ${Wt_DBO_DEBUG_LIBRARY}
    ${Wt_DBOSQLITE3_DEBUG_LIBRARY}
  )

install(TARGETS server RUNTIME DESTINATION bin)

