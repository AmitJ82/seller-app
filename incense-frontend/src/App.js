import React, { useState, useEffect } from 'react';
import {
  ShoppingCart,
  Package,
  Users,
  TrendingUp,
  Plus,
  Edit,
  Trash2,
  FileText,
  AlertCircle,
  DollarSign,
  BarChart3,
  Upload,
  Download,
  X
} from 'lucide-react';
import Dashboard from './components/Dashboard';
import Categories from './components/Categories';
import Items from './components/Items';
import Orders from './components/Orders';
import Reports from './components/Reports';
// Mock API service
import api from './services/api';
import './index.css';



// Dashboard Component


// Main App Component
const App = () => {
  const [activeTab, setActiveTab] = useState('dashboard');

  const navigation = [
    { id: 'dashboard', name: 'Dashboard', icon: BarChart3 },
    { id: 'categories', name: 'Categories', icon: Package },
    { id: 'items', name: 'Items', icon: Package },
    { id: 'orders', name: 'Orders', icon: ShoppingCart },
    { id: 'reports', name: 'Reports', icon: FileText },
  ];

  const renderContent = () => {
    switch (activeTab) {
      case 'dashboard':
        return <Dashboard />;
      case 'categories':
        return <Categories />;
      case 'items':
        return <Items />;
      case 'orders':
        return <Orders />;
      case 'reports':
        return <Reports />;
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="flex h-screen bg-gray-100">
      {/* Sidebar */}
      <div className="w-64 bg-white shadow-md">
        <div className="p-6">
          <h2 className="text-2xl font-bold text-gray-800">E-commerce Seller</h2>
        </div>

        <nav className="mt-6">
          {navigation.map((item) => {
            const Icon = item.icon;
            return (
              <button
                key={item.id}
                onClick={() => setActiveTab(item.id)}
                className={`w-full flex items-center px-6 py-3 text-left hover:bg-gray-50 ${
                  activeTab === item.id
                    ? 'bg-blue-50 border-r-4 border-blue-500 text-blue-700'
                    : 'text-gray-600'
                }`}
              >
                <Icon className="mr-3 h-5 w-5" />
                {item.name}
              </button>
            );
          })}
        </nav>
      </div>

      {/* Main Content */}
      <div className="flex-1 overflow-auto">
        {renderContent()}
      </div>
    </div>
  );
};

export default App;