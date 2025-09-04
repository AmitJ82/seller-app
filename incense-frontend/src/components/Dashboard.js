import React, { useState, useEffect } from 'react';
import { ShoppingCart, DollarSign, Package, AlertCircle } from 'lucide-react';
import api from '../services/api';

  const Dashboard = () => {
    const [stats, setStats] = useState({
      totalOrders: 0,
      totalRevenue: 0,
      totalItems: 0,
      lowStockItems: 0
    });

    useEffect(() => {
      fetchDashboardStats();
    }, []);

    const fetchDashboardStats = async () => {
      try {
        const [orders, items, lowStock] = await Promise.all([
          api.get('/orders'),
          api.get('/items'),
          api.get('/items/low-stock?threshold=10')
        ]);

        const totalRevenue = orders.reduce((sum, order) => sum + (order.finalAmount || 0), 0);

        setStats({
          totalOrders: orders.length,
          totalRevenue: totalRevenue,
          totalItems: items.length,
          lowStockItems: lowStock.length
        });
      } catch (error) {
        console.error('Error fetching dashboard stats:', error);
      }
    };

    return (
      <div className="p-6">
        <h1 className="text-3xl font-bold text-gray-800 mb-8">Dashboard</h1>
        <div> My Dashboard</div>
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6 mb-8">
          <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-blue-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Total Orders</p>
                <p className="text-3xl font-bold text-gray-900">{stats.totalOrders}</p>
              </div>
              <ShoppingCart className="h-12 w-12 text-blue-500" />
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-green-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Total Revenue</p>
                <p className="text-3xl font-bold text-gray-900">₹{stats.totalRevenue.toLocaleString()}</p>
              </div>
              <DollarSign className="h-12 w-12 text-green-500" />
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-purple-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Total Items</p>
                <p className="text-3xl font-bold text-gray-900">{stats.totalItems}</p>
              </div>
              <Package className="h-12 w-12 text-purple-500" />
            </div>
          </div>

          <div className="bg-white p-6 rounded-lg shadow-md border-l-4 border-red-500">
            <div className="flex items-center justify-between">
              <div>
                <p className="text-sm font-medium text-gray-600">Low Stock Items</p>
                <p className="text-3xl font-bold text-gray-900">{stats.lowStockItems}</p>
              </div>
              <AlertCircle className="h-12 w-12 text-red-500" />
            </div>
          </div>
        </div>
      </div>
    );
  };

export default Dashboard;