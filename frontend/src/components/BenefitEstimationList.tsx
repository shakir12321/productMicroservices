"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  BenefitEstimation,
  Order,
  benefitEstimationApiFunctions,
  orderApiFunctions,
} from "@/lib/api";
import {
  Plus,
  Edit,
  Trash2,
  Gift,
  DollarSign,
  Calendar,
  Hash,
} from "lucide-react";

export default function BenefitEstimationList() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingEstimation, setEditingEstimation] =
    useState<BenefitEstimation | null>(null);
  const [formData, setFormData] = useState<Partial<BenefitEstimation>>({
    orderId: 0,
    benefitType: "LOYALTY_POINTS",
    estimatedValue: 0,
    status: "PENDING",
  });

  const queryClient = useQueryClient();

  // Fetch benefit estimations
  const {
    data: response,
    isLoading,
    error,
  } = useQuery({
    queryKey: ["benefit-estimations"],
    queryFn: benefitEstimationApiFunctions.getAllEstimations,
  });

  const estimations = response?.data?.data || [];

  // Fetch orders for estimation creation
  const { data: ordersResponse } = useQuery({
    queryKey: ["orders"],
    queryFn: orderApiFunctions.getAllOrders,
  });

  const orders = ordersResponse?.data?.data || [];

  // Create estimation mutation
  const createMutation = useMutation({
    mutationFn: benefitEstimationApiFunctions.createEstimation,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["benefit-estimations"] });
      setIsModalOpen(false);
      resetForm();
    },
  });

  // Update estimation mutation
  const updateMutation = useMutation({
    mutationFn: ({
      id,
      estimation,
    }: {
      id: number;
      estimation: BenefitEstimation;
    }) => benefitEstimationApiFunctions.createEstimation(estimation), // Note: API might need update endpoint
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["benefit-estimations"] });
      setIsModalOpen(false);
      setEditingEstimation(null);
      resetForm();
    },
  });

  // Delete estimation mutation
  const deleteMutation = useMutation({
    mutationFn: (_id: number) => Promise.resolve(), // Note: API might need delete endpoint
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["benefit-estimations"] });
    },
  });

  const resetForm = () => {
    setFormData({
      orderId: 0,
      benefitType: "LOYALTY_POINTS",
      estimatedValue: 0,
      status: "PENDING",
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingEstimation) {
      updateMutation.mutate({
        id: editingEstimation.id!,
        estimation: formData as BenefitEstimation,
      });
    } else {
      createMutation.mutate(formData as BenefitEstimation);
    }
  };

  const handleEdit = (estimation: BenefitEstimation) => {
    setEditingEstimation(estimation);
    setFormData(estimation);
    setIsModalOpen(true);
  };

  const handleDelete = (id: number) => {
    if (confirm("Are you sure you want to delete this estimation?")) {
      deleteMutation.mutate(id);
    }
  };

  const getBenefitTypeColor = (type: string) => {
    switch (type) {
      case "LOYALTY_POINTS":
        return "bg-blue-100 text-blue-800";
      case "CASHBACK":
        return "bg-green-100 text-green-800";
      case "DISCOUNT":
        return "bg-purple-100 text-purple-800";
      case "FREE_SHIPPING":
        return "bg-orange-100 text-orange-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PENDING":
        return "bg-yellow-100 text-yellow-800";
      case "CALCULATED":
        return "bg-blue-100 text-blue-800";
      case "APPLIED":
        return "bg-green-100 text-green-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const formatBenefitType = (type: string) => {
    return type
      .replace("_", " ")
      .toLowerCase()
      .replace(/\b\w/g, (l) => l.toUpperCase());
  };

  if (isLoading)
    return (
      <div className="text-center py-8">Loading benefit estimations...</div>
    );
  if (error)
    return (
      <div className="text-center py-8 text-red-600">
        Error loading benefit estimations
      </div>
    );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">
          Benefit Estimations
        </h2>
        <button
          onClick={() => setIsModalOpen(true)}
          className="bg-purple-600 text-white px-4 py-2 rounded-lg flex items-center gap-2 hover:bg-purple-700"
        >
          <Plus size={20} />
          Create Estimation
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {estimations && Array.isArray(estimations) && estimations.length > 0 ? (
          estimations.map((estimation: BenefitEstimation, index: number) => (
            <div
              key={estimation.id || index}
              className="bg-white rounded-lg shadow-md p-6 border"
            >
              <div className="flex justify-between items-start mb-4">
                <h3 className="text-lg font-semibold text-gray-900">
                  Estimation #{estimation.id}
                </h3>
                <div className="flex gap-2">
                  <button
                    onClick={() => handleEdit(estimation)}
                    className="text-blue-600 hover:text-blue-800"
                  >
                    <Edit size={16} />
                  </button>
                  <button
                    onClick={() => handleDelete(estimation.id!)}
                    className="text-red-600 hover:text-red-800"
                  >
                    <Trash2 size={16} />
                  </button>
                </div>
              </div>

              <div className="space-y-3">
                <div className="flex items-center gap-2 text-sm">
                  <Hash size={16} className="text-gray-600" />
                  <span>Order ID: {estimation.orderId}</span>
                </div>

                <div className="flex items-center gap-2 text-sm">
                  <Gift size={16} className="text-purple-600" />
                  <span
                    className={`px-2 py-1 rounded-full text-xs font-medium ${getBenefitTypeColor(
                      estimation.benefitType
                    )}`}
                  >
                    {formatBenefitType(estimation.benefitType)}
                  </span>
                </div>

                <div className="flex items-center gap-2 text-sm">
                  <DollarSign size={16} className="text-green-600" />
                  <span className="font-medium">
                    ${estimation.estimatedValue}
                  </span>
                </div>

                <div className="flex items-center gap-2 text-sm">
                  <span
                    className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(
                      estimation.status
                    )}`}
                  >
                    {estimation.status}
                  </span>
                </div>

                {estimation.createdAt && (
                  <div className="flex items-center gap-2 text-sm text-gray-600">
                    <Calendar size={16} />
                    <span>
                      {new Date(estimation.createdAt).toLocaleDateString()}
                    </span>
                  </div>
                )}
              </div>
            </div>
          ))
        ) : (
          <div className="col-span-full text-center py-8 text-gray-500">
            No benefit estimations found.
          </div>
        )}
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">
              {editingEstimation ? "Edit Estimation" : "Create Estimation"}
            </h3>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Order
                </label>
                <select
                  value={formData.orderId}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      orderId: parseInt(e.target.value),
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  required
                >
                  <option value={0}>Select Order</option>
                  {orders && Array.isArray(orders) && orders.map((order: Order) => (
                    <option key={order.id} value={order.id}>
                      Order #{order.id} - {order.customerName} ($
                      {order.totalAmount})
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Benefit Type
                </label>
                <select
                  value={formData.benefitType}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      benefitType: e.target
                        .value as BenefitEstimation["benefitType"],
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                >
                  <option value="LOYALTY_POINTS">Loyalty Points</option>
                  <option value="CASHBACK">Cashback</option>
                  <option value="DISCOUNT">Discount</option>
                  <option value="FREE_SHIPPING">Free Shipping</option>
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Estimated Value
                </label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.estimatedValue}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      estimatedValue: parseFloat(e.target.value),
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Status
                </label>
                <select
                  value={formData.status}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      status: e.target.value as BenefitEstimation["status"],
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                >
                  <option value="PENDING">Pending</option>
                  <option value="CALCULATED">Calculated</option>
                  <option value="APPLIED">Applied</option>
                </select>
              </div>

              <div className="flex gap-3 pt-4">
                <button
                  type="submit"
                  className="flex-1 bg-purple-600 text-white py-2 px-4 rounded-md hover:bg-purple-700"
                  disabled={
                    createMutation.isPending || updateMutation.isPending
                  }
                >
                  {createMutation.isPending || updateMutation.isPending
                    ? "Saving..."
                    : "Save"}
                </button>
                <button
                  type="button"
                  onClick={() => {
                    setIsModalOpen(false);
                    setEditingEstimation(null);
                    resetForm();
                  }}
                  className="flex-1 bg-gray-300 text-gray-700 py-2 px-4 rounded-md hover:bg-gray-400"
                >
                  Cancel
                </button>
              </div>
            </form>
          </div>
        </div>
      )}
    </div>
  );
}
