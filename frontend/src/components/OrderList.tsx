import React from "react";

const OrderList: React.FC = () => {
  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">
          Orders - Hello World
        </h2>
      </div>

      <div className="bg-blue-100 p-8 rounded-lg text-center">
        <h3 className="text-3xl font-bold text-blue-800 mb-4">Hello World!</h3>
        <p className="text-lg text-blue-600">This is a static test page.</p>
        <p className="text-sm text-blue-500 mt-2">
          If you can see this, the component is working.
        </p>
      </div>
    </div>
  );
};

export default OrderList;
