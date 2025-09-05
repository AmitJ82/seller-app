import  React, { useState, useEffect } from 'react';
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
import api from '../services/api';
import CsvUploadModal from './common';
// Orders Component
const Orders = () => {
  const [orders, setOrders] = useState([]);
  const [items, setItems] = useState([]);
  const [showForm, setShowForm] = useState(false);
  const [showBulkForm, setShowBulkForm] = useState(false);

  const [showCsvUpload, setShowCsvUpload] = useState(false);
  const [formData, setFormData] = useState({
    buyerName: '',
    buyerMobile: '',
    buyerEmail: '', // Added email field
    venue: '',
    orderItems: [{ itemId: '', quantity: 1, salePrice: '', discount: 0 }]
  });
  const [bulkFormData, setBulkFormData] = useState({
    orderDate: new Date().toISOString().slice(0, 16),
    items: [{ itemId: '', quantity: 1 }]
  });

  useEffect(() => {
    fetchOrders();
    fetchItems();
  }, []);

  const fetchOrders = async () => {
    const data = await api.get('/orders');
    setOrders(data);
  };

  const fetchItems = async () => {
    const data = await api.get('/items');
    setItems(data);
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    try {
      const orderData = {
        ...formData,
        orderItems: formData.orderItems.map(item => ({
          ...item,
          itemId: parseInt(item.itemId),
          quantity: parseInt(item.quantity),
          salePrice: parseFloat(item.salePrice || 0),
          discount: parseFloat(item.discount || 0)
        }))
      };

      await api.post('/orders', orderData);
      resetForm();
      fetchOrders();
      alert('Order created successfully!');
    } catch (error) {
      alert('Error creating order');
    }
  };

  const handleBulkSubmit = async (e) => {
    e.preventDefault();
    try {
      const bulkData = {
        ...bulkFormData,
        orderDate: new Date(bulkFormData.orderDate).toISOString(),
        items: bulkFormData.items.map(item => ({
          itemId: parseInt(item.itemId),
          quantity: parseInt(item.quantity)
        }))
      };

      await api.post('/orders/bulk', bulkData);
      resetBulkForm();
      fetchOrders();
      alert('Bulk order processed successfully!');
    } catch (error) {
      alert('Error processing bulk order');
    }
  };

  const addOrderItem = () => {
    setFormData({
      ...formData,
      orderItems: [...formData.orderItems, { itemId: '', quantity: 1, salePrice: '', discount: 0 }]
    });
  };

  const removeOrderItem = (index) => {
    setFormData({
      ...formData,
      orderItems: formData.orderItems.filter((_, i) => i !== index)
    });
  };

  const updateOrderItem = (index, field, value) => {
    const updatedItems = [...formData.orderItems];
    updatedItems[index][field] = value;

    // Auto-fill sale price when item is selected
    if (field === 'itemId' && value) {
      const selectedItem = items.find(item => item.id == value);
      if (selectedItem) {
        updatedItems[index].salePrice = selectedItem.sellingPrice;
      }
    }

    setFormData({ ...formData, orderItems: updatedItems });
  };

  const addBulkItem = () => {
    setBulkFormData({
      ...bulkFormData,
      items: [...bulkFormData.items, { itemId: '', quantity: 1 }]
    });
  };

  const removeBulkItem = (index) => {
    setBulkFormData({
      ...bulkFormData,
      items: bulkFormData.items.filter((_, i) => i !== index)
    });
  };

  const updateBulkItem = (index, field, value) => {
    const updatedItems = [...bulkFormData.items];
    updatedItems[index][field] = value;
    setBulkFormData({ ...bulkFormData, items: updatedItems });
  };

  const resetForm = () => {
    setFormData({
      buyerName: '',
      buyerMobile: '',
      buyerEmail: '', // Reset email field
      venue: '',
      orderItems: [{ itemId: '', quantity: 1, salePrice: '', discount: 0 }]
    });
    setShowForm(false);
  };

  const resetBulkForm = () => {
    setBulkFormData({
      orderDate: new Date().toISOString().slice(0, 16),
      items: [{ itemId: '', quantity: 1 }]
    });
    setShowBulkForm(false);
  };

  return (
    <div className="p-6">
      <div className="flex justify-between items-center mb-6">
        <h1 className="text-3xl font-bold text-gray-800">Orders</h1>
        <div className="flex gap-2">
          <button
                      onClick={() => setShowCsvUpload(true)}
                      className="bg-purple-500 hover:bg-purple-600 text-white px-4 py-2 rounded-lg flex items-center gap-2"
                    >
                      <Upload size={20} />
                      Import CSV
                    </button>
          <button
            onClick={() => setShowBulkForm(true)}
            className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-lg flex items-center gap-2"
          >
            <Plus size={20} />
            Bulk Order
          </button>
          <button
            onClick={() => setShowForm(true)}
            className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-lg flex items-center gap-2"
          >
            <Plus size={20} />
            Create Order
          </button>
        </div>
      </div>
     <CsvUploadModal
            isOpen={showCsvUpload}
            onClose={() => setShowCsvUpload(false)}
            type="orders"
            onUploadSuccess={() => {
              fetchOrders();
              setShowCsvUpload(false);
            }}
          />
      {/* Regular Order Form */}
      {showForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-full max-w-4xl max-h-96 overflow-y-auto">
            <h2 className="text-xl font-bold mb-4">Create New Order</h2>
            <form onSubmit={handleSubmit}>
              <div className="grid grid-cols-3 gap-4 mb-4">
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Buyer Name</label>
                  <input
                    type="text"
                    value={formData.buyerName}
                    onChange={(e) => setFormData({...formData, buyerName: e.target.value})}
                    className="w-full p-2 border border-gray-300 rounded-md"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Buyer Mobile</label>
                  <input
                    type="text"
                    value={formData.buyerMobile}
                    onChange={(e) => setFormData({...formData, buyerMobile: e.target.value})}
                    className="w-full p-2 border border-gray-300 rounded-md"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Buyer Email</label>
                  <input
                    type="email"
                    value={formData.buyerEmail}
                    onChange={(e) => setFormData({...formData, buyerEmail: e.target.value})}
                    className="w-full p-2 border border-gray-300 rounded-md"
                    required
                  />
                </div>
                <div>
                  <label className="block text-sm font-medium text-gray-700 mb-2">Venue</label>
                  <input
                    type="text"
                    value={formData.venue}
                    onChange={(e) => setFormData({...formData, venue: e.target.value})}
                    className="w-full p-2 border border-gray-300 rounded-md"
                  />
                </div>
              </div>

              <div className="mb-4">
                <div className="flex justify-between items-center mb-2">
                  <h3 className="text-lg font-medium">Order Items</h3>
                  <button
                    type="button"
                    onClick={addOrderItem}
                    className="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded text-sm"
                  >
                    Add Item
                  </button>
                </div>

                {formData.orderItems.map((orderItem, index) => (
                  <div key={index} className="grid grid-cols-6 gap-2 mb-2 p-2 border rounded">
                    <select
                      value={orderItem.itemId}
                      onChange={(e) => updateOrderItem(index, 'itemId', e.target.value)}
                      className="p-2 border border-gray-300 rounded-md"
                      required
                    >
                      <option value="">Select Item</option>
                      {items.map((item) => (
                        <option key={item.id} value={item.id}>
                          {item.name} (Stock: {item.quantityInStock})
                        </option>
                      ))}
                    </select>
                    <input
                      type="number"
                      placeholder="Quantity"
                      value={orderItem.quantity}
                      onChange={(e) => updateOrderItem(index, 'quantity', e.target.value)}
                      className="p-2 border border-gray-300 rounded-md"
                      min="1"
                      required
                    />
                    <input
                      type="number"
                      step="0.01"
                      placeholder="Sale Price"
                      value={orderItem.salePrice}
                      onChange={(e) => updateOrderItem(index, 'salePrice', e.target.value)}
                      className="p-2 border border-gray-300 rounded-md"
                      required
                    />
                    <input
                      type="number"
                      step="0.01"
                      placeholder="Discount"
                      value={orderItem.discount}
                      onChange={(e) => updateOrderItem(index, 'discount', e.target.value)}
                      className="p-2 border border-gray-300 rounded-md"
                    />
                    <div className="text-sm p-2">
                      Total: ₹{((orderItem.salePrice || 0) * (orderItem.quantity || 0) - (orderItem.discount || 0)).toFixed(2)}
                    </div>
                    <button
                      type="button"
                      onClick={() => removeOrderItem(index)}
                      className="text-red-500 hover:text-red-700"
                      disabled={formData.orderItems.length === 1}
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                ))}
              </div>

              <div className="flex gap-2">
                <button
                  type="submit"
                  className="bg-blue-500 hover:bg-blue-600 text-white px-4 py-2 rounded-md"
                >
                  Create Order
                </button>
                <button
                  type="button"
                  onClick={resetForm}
                  className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Bulk Order Form */}
      {showBulkForm && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white p-6 rounded-lg w-full max-w-2xl max-h-96 overflow-y-auto">
            <h2 className="text-xl font-bold mb-4">Bulk Order Processing</h2>
            <form onSubmit={handleBulkSubmit}>
              <div className="mb-4">
                <label className="block text-sm font-medium text-gray-700 mb-2">Order Date</label>
                <input
                  type="datetime-local"
                  value={bulkFormData.orderDate}
                  onChange={(e) => setBulkFormData({...bulkFormData, orderDate: e.target.value})}
                  className="w-full p-2 border border-gray-300 rounded-md"
                  required
                />
              </div>

              <div className="mb-4">
                <div className="flex justify-between items-center mb-2">
                  <h3 className="text-lg font-medium">Items</h3>
                  <button
                    type="button"
                    onClick={addBulkItem}
                    className="bg-green-500 hover:bg-green-600 text-white px-3 py-1 rounded text-sm"
                  >
                    Add Item
                  </button>
                </div>

                {bulkFormData.items.map((item, index) => (
                  <div key={index} className="grid grid-cols-4 gap-2 mb-2 p-2 border rounded">
                    <select
                      value={item.itemId}
                      onChange={(e) => updateBulkItem(index, 'itemId', e.target.value)}
                      className="col-span-2 p-2 border border-gray-300 rounded-md"
                      required
                    >
                      <option value="">Select Item</option>
                      {items.map((availableItem) => (
                        <option key={availableItem.id} value={availableItem.id}>
                          {availableItem.name} (Stock: {availableItem.quantityInStock})
                        </option>
                      ))}
                    </select>
                    <input
                      type="number"
                      placeholder="Quantity"
                      value={item.quantity}
                      onChange={(e) => updateBulkItem(index, 'quantity', e.target.value)}
                      className="p-2 border border-gray-300 rounded-md"
                      min="1"
                      required
                    />
                    <button
                      type="button"
                      onClick={() => removeBulkItem(index)}
                      className="text-red-500 hover:text-red-700 flex justify-center items-center"
                      disabled={bulkFormData.items.length === 1}
                    >
                      <Trash2 size={16} />
                    </button>
                  </div>
                ))}
              </div>

              <div className="flex gap-2">
                <button
                  type="submit"
                  className="bg-green-500 hover:bg-green-600 text-white px-4 py-2 rounded-md"
                >
                  Process Bulk Order
                </button>
                <button
                  type="button"
                  onClick={resetBulkForm}
                  className="bg-gray-500 hover:bg-gray-600 text-white px-4 py-2 rounded-md"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}

      {/* Orders List */}
      <div className="overflow-x-auto">
        <table className="min-w-full bg-white border border-gray-200">
          <thead className="bg-gray-50">
            <tr>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Order ID</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Buyer</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Date</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Items</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Total Amount</th>
              <th className="px-6 py-3 text-left text-xs font-medium text-gray-500 uppercase tracking-wider">Actions</th>
            </tr>
          </thead>
          <tbody className="bg-white divide-y divide-gray-200">
            {orders.map((order) => (
              <tr key={order.id}>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  #{order.id}
                </td>
                <td className="px-6 py-4 whitespace-nowrap">
                  <div>
                    <div className="text-sm font-medium text-gray-900">{order.buyerName}</div>
                    <div className="text-sm text-gray-500">{order.buyerMobile}</div>
                  </div>
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {new Date(order.orderDate).toLocaleDateString()}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm text-gray-900">
                  {order.orderItems?.length || 0} items
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium text-gray-900">
                  ₹{order.finalAmount?.toFixed(2) || '0.00'}
                </td>
                <td className="px-6 py-4 whitespace-nowrap text-sm font-medium">
                  <button className="text-blue-600 hover:text-blue-900" onClick={() =>   alert('Items: ' + order.orderItems.map(item => `${item.itemName} (Qty: ${item.quantity})`).join(', '))}>
                    <FileText size={16} />
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
      </div>
    </div>
  );
};
export default Orders;