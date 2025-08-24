"use client";

import { useState } from "react";
import { useQuery, useMutation, useQueryClient } from "@tanstack/react-query";
import {
  Payout,
  payoutApiFunctions,
  benefitEstimationApiFunctions,
} from "@/lib/api";
import {
  Plus,
  Edit,
  Trash2,
  CreditCard,
  DollarSign,
  Calendar,
  Hash,
} from "lucide-react";

export default function PayoutList() {
  const [isModalOpen, setIsModalOpen] = useState(false);
  const [editingPayout, setEditingPayout] = useState<Payout | null>(null);
  const [formData, setFormData] = useState<Partial<Payout>>({
    benefitEstimationId: 0,
    amount: 0,
    payoutMethod: "BANK_TRANSFER",
    status: "PENDING",
  });

  const queryClient = useQueryClient();

  // Fetch payouts
  const {
    data: payouts,
    isLoading,
    error,
  } = useQuery({
    queryKey: ["payouts"],
    queryFn: payoutApiFunctions.getAllPayouts,
  });

  // Fetch benefit estimations for payout creation
  const { data: estimations } = useQuery({
    queryKey: ["benefit-estimations"],
    queryFn: benefitEstimationApiFunctions.getAllEstimations,
  });

  // Create payout mutation
  const createMutation = useMutation({
    mutationFn: payoutApiFunctions.createPayout,
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payouts"] });
      setIsModalOpen(false);
      resetForm();
    },
  });

  // Update payout mutation
  const updateMutation = useMutation({
    mutationFn: ({ id, payout }: { id: number; payout: Payout }) =>
      payoutApiFunctions.createPayout(payout), // Note: API might need update endpoint
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payouts"] });
      setIsModalOpen(false);
      setEditingPayout(null);
      resetForm();
    },
  });

  // Delete payout mutation
  const deleteMutation = useMutation({
    mutationFn: (_id: number) => Promise.resolve(), // Note: API might need delete endpoint
    onSuccess: () => {
      queryClient.invalidateQueries({ queryKey: ["payouts"] });
    },
  });

  const resetForm = () => {
    setFormData({
      benefitEstimationId: 0,
      amount: 0,
      payoutMethod: "BANK_TRANSFER",
      status: "PENDING",
    });
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    if (editingPayout) {
      updateMutation.mutate({
        id: editingPayout.id!,
        payout: formData as Payout,
      });
    } else {
      createMutation.mutate(formData as Payout);
    }
  };

  const handleEdit = (payout: Payout) => {
    setEditingPayout(payout);
    setFormData(payout);
    setIsModalOpen(true);
  };

  const handleDelete = (id: number) => {
    if (confirm("Are you sure you want to delete this payout?")) {
      deleteMutation.mutate(id);
    }
  };

  const getPayoutMethodColor = (method: string) => {
    switch (method) {
      case "BANK_TRANSFER":
        return "bg-blue-100 text-blue-800";
      case "CREDIT_CARD":
        return "bg-green-100 text-green-800";
      case "DIGITAL_WALLET":
        return "bg-purple-100 text-purple-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const getStatusColor = (status: string) => {
    switch (status) {
      case "PENDING":
        return "bg-yellow-100 text-yellow-800";
      case "PROCESSING":
        return "bg-blue-100 text-blue-800";
      case "COMPLETED":
        return "bg-green-100 text-green-800";
      case "FAILED":
        return "bg-red-100 text-red-800";
      default:
        return "bg-gray-100 text-gray-800";
    }
  };

  const formatPayoutMethod = (method: string) => {
    return method
      .replace("_", " ")
      .toLowerCase()
      .replace(/\b\w/g, (l) => l.toUpperCase());
  };

  if (isLoading)
    return <div className="text-center py-8">Loading payouts...</div>;
  if (error)
    return (
      <div className="text-center py-8 text-red-600">Error loading payouts</div>
    );

  return (
    <div className="space-y-6">
      <div className="flex justify-between items-center">
        <h2 className="text-2xl font-bold text-gray-900">Payouts</h2>
        <button
          onClick={() => setIsModalOpen(true)}
          className="bg-orange-600 text-white px-4 py-2 rounded-lg flex items-center gap-2 hover:bg-orange-700"
        >
          <Plus size={20} />
          Create Payout
        </button>
      </div>

      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
        {payouts?.data?.map((payout) => (
          <div
            key={payout.id}
            className="bg-white rounded-lg shadow-md p-6 border"
          >
            <div className="flex justify-between items-start mb-4">
              <h3 className="text-lg font-semibold text-gray-900">
                Payout #{payout.id}
              </h3>
              <div className="flex gap-2">
                <button
                  onClick={() => handleEdit(payout)}
                  className="text-blue-600 hover:text-blue-800"
                >
                  <Edit size={16} />
                </button>
                <button
                  onClick={() => handleDelete(payout.id!)}
                  className="text-red-600 hover:text-red-800"
                >
                  <Trash2 size={16} />
                </button>
              </div>
            </div>

            <div className="space-y-3">
              <div className="flex items-center gap-2 text-sm">
                <Hash size={16} className="text-gray-600" />
                <span>Estimation ID: {payout.benefitEstimationId}</span>
              </div>

              <div className="flex items-center gap-2 text-sm">
                <DollarSign size={16} className="text-green-600" />
                <span className="font-medium">${payout.amount}</span>
              </div>

              <div className="flex items-center gap-2 text-sm">
                <CreditCard size={16} className="text-blue-600" />
                <span
                  className={`px-2 py-1 rounded-full text-xs font-medium ${getPayoutMethodColor(
                    payout.payoutMethod
                  )}`}
                >
                  {formatPayoutMethod(payout.payoutMethod)}
                </span>
              </div>

              <div className="flex items-center gap-2 text-sm">
                <span
                  className={`px-2 py-1 rounded-full text-xs font-medium ${getStatusColor(
                    payout.status
                  )}`}
                >
                  {payout.status}
                </span>
              </div>

              {payout.createdAt && (
                <div className="flex items-center gap-2 text-sm text-gray-600">
                  <Calendar size={16} />
                  <span>{new Date(payout.createdAt).toLocaleDateString()}</span>
                </div>
              )}
            </div>
          </div>
        ))}
      </div>

      {/* Modal */}
      {isModalOpen && (
        <div className="fixed inset-0 bg-black bg-opacity-50 flex items-center justify-center z-50">
          <div className="bg-white rounded-lg p-6 w-full max-w-md">
            <h3 className="text-lg font-semibold mb-4">
              {editingPayout ? "Edit Payout" : "Create Payout"}
            </h3>

            <form onSubmit={handleSubmit} className="space-y-4">
              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Benefit Estimation
                </label>
                <select
                  value={formData.benefitEstimationId}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      benefitEstimationId: parseInt(e.target.value),
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  required
                >
                  <option value={0}>Select Estimation</option>
                  {estimations?.data?.map((estimation) => (
                    <option key={estimation.id} value={estimation.id}>
                      Estimation #{estimation.id} - ${estimation.estimatedValue}
                    </option>
                  ))}
                </select>
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Amount
                </label>
                <input
                  type="number"
                  step="0.01"
                  value={formData.amount}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      amount: parseFloat(e.target.value),
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                  required
                />
              </div>

              <div>
                <label className="block text-sm font-medium text-gray-700">
                  Payout Method
                </label>
                <select
                  value={formData.payoutMethod}
                  onChange={(e) =>
                    setFormData({
                      ...formData,
                      payoutMethod: e.target.value as Payout["payoutMethod"],
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                >
                  <option value="BANK_TRANSFER">Bank Transfer</option>
                  <option value="CREDIT_CARD">Credit Card</option>
                  <option value="DIGITAL_WALLET">Digital Wallet</option>
                </select>
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
                      status: e.target.value as Payout["status"],
                    })
                  }
                  className="mt-1 block w-full rounded-md border-gray-300 shadow-sm focus:border-blue-500 focus:ring-blue-500"
                >
                  <option value="PENDING">Pending</option>
                  <option value="PROCESSING">Processing</option>
                  <option value="COMPLETED">Completed</option>
                  <option value="FAILED">Failed</option>
                </select>
              </div>

              <div className="flex gap-3 pt-4">
                <button
                  type="submit"
                  className="flex-1 bg-orange-600 text-white py-2 px-4 rounded-md hover:bg-orange-700"
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
                    setEditingPayout(null);
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
