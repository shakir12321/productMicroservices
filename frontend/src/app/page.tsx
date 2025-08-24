"use client";

import { useState } from "react";
import ProductList from "@/components/ProductList";
import OrderList from "@/components/OrderList";
import BenefitEstimationList from "@/components/BenefitEstimationList";
import PayoutList from "@/components/PayoutList";
import {
  Package,
  ShoppingCart,
  Gift,
  CreditCard,
  BarChart3,
} from "lucide-react";

type TabType = "products" | "orders" | "benefits" | "payouts" | "dashboard";

export default function Home() {
  const [activeTab, setActiveTab] = useState<TabType>("dashboard");

  const tabs = [
    { id: "dashboard", label: "Dashboard", icon: BarChart3 },
    { id: "products", label: "Products", icon: Package },
    { id: "orders", label: "Orders", icon: ShoppingCart },
    { id: "benefits", label: "Benefits", icon: Gift },
    { id: "payouts", label: "Payouts", icon: CreditCard },
  ];

  const renderContent = () => {
    switch (activeTab) {
      case "products":
        return <ProductList />;
      case "orders":
        return <OrderList />;
      case "benefits":
        return <BenefitEstimationList />;
      case "payouts":
        return <PayoutList />;
      case "dashboard":
      default:
        return <Dashboard />;
    }
  };

  return (
    <div className="min-h-screen bg-gray-50">
      {/* Header */}
      <header className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex justify-between items-center h-16">
            <div className="flex items-center">
              <h1 className="text-xl font-semibold text-gray-900">
                Microservices Dashboard
              </h1>
            </div>
            <div className="flex items-center space-x-4">
              <span className="text-sm text-gray-500">
                Connected to Spring Boot Microservices
              </span>
            </div>
          </div>
        </div>
      </header>

      {/* Navigation */}
      <nav className="bg-white shadow-sm border-b">
        <div className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8">
          <div className="flex space-x-8">
            {tabs.map((tab) => {
              const Icon = tab.icon;
              return (
                <button
                  key={tab.id}
                  onClick={() => setActiveTab(tab.id as TabType)}
                  className={`flex items-center space-x-2 py-4 px-1 border-b-2 font-medium text-sm ${
                    activeTab === tab.id
                      ? "border-blue-500 text-blue-600"
                      : "border-transparent text-gray-500 hover:text-gray-700 hover:border-gray-300"
                  }`}
                >
                  <Icon size={20} />
                  <span>{tab.label}</span>
                </button>
              );
            })}
          </div>
        </div>
      </nav>

      {/* Main Content */}
      <main className="max-w-7xl mx-auto py-6 sm:px-6 lg:px-8">
        <div className="px-4 py-6 sm:px-0">{renderContent()}</div>
      </main>
    </div>
  );
}

function Dashboard() {
  return (
    <div className="space-y-6">
      <div>
        <h2 className="text-2xl font-bold text-gray-900 mb-4">
          Dashboard Overview
        </h2>
        <p className="text-gray-600 mb-6">
          Welcome to the Microservices Dashboard. This application connects to
          four Spring Boot microservices:
        </p>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-6">
        <div className="bg-white rounded-lg shadow-md p-6 border">
          <div className="flex items-center">
            <div className="p-2 bg-blue-100 rounded-lg">
              <Package className="h-6 w-6 text-blue-600" />
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-semibold text-gray-900">
                Product Service
              </h3>
              <p className="text-sm text-gray-600">Port 8081</p>
            </div>
          </div>
          <p className="mt-4 text-sm text-gray-600">
            Manage product catalog, inventory, and stock levels
          </p>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 border">
          <div className="flex items-center">
            <div className="p-2 bg-green-100 rounded-lg">
              <ShoppingCart className="h-6 w-6 text-green-600" />
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-semibold text-gray-900">
                Order Service
              </h3>
              <p className="text-sm text-gray-600">Port 8082</p>
            </div>
          </div>
          <p className="mt-4 text-sm text-gray-600">
            Process orders and manage customer transactions
          </p>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 border">
          <div className="flex items-center">
            <div className="p-2 bg-purple-100 rounded-lg">
              <Gift className="h-6 w-6 text-purple-600" />
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-semibold text-gray-900">
                Benefit Estimation
              </h3>
              <p className="text-sm text-gray-600">Port 8083</p>
            </div>
          </div>
          <p className="mt-4 text-sm text-gray-600">
            Calculate loyalty points, cashback, and discounts
          </p>
        </div>

        <div className="bg-white rounded-lg shadow-md p-6 border">
          <div className="flex items-center">
            <div className="p-2 bg-orange-100 rounded-lg">
              <CreditCard className="h-6 w-6 text-orange-600" />
            </div>
            <div className="ml-4">
              <h3 className="text-lg font-semibold text-gray-900">
                Payout Service
              </h3>
              <p className="text-sm text-gray-600">Port 8084</p>
            </div>
          </div>
          <p className="mt-4 text-sm text-gray-600">
            Process payouts and manage payment methods
          </p>
        </div>
      </div>

      <div className="bg-white rounded-lg shadow-md p-6 border">
        <h3 className="text-lg font-semibold text-gray-900 mb-4">
          Getting Started
        </h3>
        <div className="space-y-3 text-sm text-gray-600">
          <p>
            1. <strong>Products:</strong> Add products to your catalog with
            pricing and inventory
          </p>
          <p>
            2. <strong>Orders:</strong> Create orders by selecting products and
            customer details
          </p>
          <p>
            3. <strong>Benefits:</strong> Generate benefit estimations for
            customer loyalty
          </p>
          <p>
            4. <strong>Payouts:</strong> Process payouts based on benefit
            estimations
          </p>
        </div>
      </div>

      <div className="bg-blue-50 rounded-lg p-6 border border-blue-200">
        <h3 className="text-lg font-semibold text-blue-900 mb-2">
          System Status
        </h3>
        <p className="text-sm text-blue-700">
          All microservices are running on Docker containers. The frontend
          connects to the Spring Boot APIs running on localhost ports 8081-8084.
          Make sure all services are running before using the dashboard.
        </p>
      </div>
    </div>
  );
}
