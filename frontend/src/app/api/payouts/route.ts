import { NextResponse } from "next/server";

// Dummy data for payouts
const dummyPayouts = [
  {
    id: 1,
    benefitEstimationId: 1,
    amount: 150.0,
    payoutMethod: "BANK_TRANSFER",
    status: "COMPLETED",
    createdAt: "2024-08-24T10:30:00Z",
  },
  {
    id: 2,
    benefitEstimationId: 2,
    amount: 75.5,
    payoutMethod: "CREDIT_CARD",
    status: "PROCESSING",
    createdAt: "2024-08-24T11:15:00Z",
  },
  {
    id: 3,
    benefitEstimationId: 3,
    amount: 200.0,
    payoutMethod: "DIGITAL_WALLET",
    status: "PENDING",
    createdAt: "2024-08-24T12:00:00Z",
  },
  {
    id: 4,
    benefitEstimationId: 4,
    amount: 45.25,
    payoutMethod: "BANK_TRANSFER",
    status: "COMPLETED",
    createdAt: "2024-08-24T13:45:00Z",
  },
  {
    id: 5,
    benefitEstimationId: 5,
    amount: 120.75,
    payoutMethod: "CREDIT_CARD",
    status: "FAILED",
    createdAt: "2024-08-24T14:20:00Z",
  },
];

export async function GET() {
  try {
    // Try to connect to the actual microservice first
    const response = await fetch("http://payout-service:8084/api/payouts", {
      method: "GET",
      headers: {
        "Content-Type": "application/json",
      },
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json({ data });
    }
  } catch (error) {
    console.log("Microservice not available, using dummy data");
  }

  // Return dummy data if microservice is not available
  return NextResponse.json({ data: dummyPayouts });
}

export async function POST(request: Request) {
  try {
    const body = await request.json();

    // Try to connect to the actual microservice first
    const response = await fetch("http://payout-service:8084/api/payouts", {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
      },
      body: JSON.stringify(body),
    });

    if (response.ok) {
      const data = await response.json();
      return NextResponse.json(data);
    }
  } catch (error) {
    console.log("Microservice not available, using dummy response");
  }

  // Return dummy response if microservice is not available
  const newPayout = {
    id: Math.floor(Math.random() * 1000) + 6,
    ...body,
    createdAt: new Date().toISOString(),
  };

  return NextResponse.json(newPayout);
}
