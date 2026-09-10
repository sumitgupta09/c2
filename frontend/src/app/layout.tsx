import type { Metadata } from "next";
import AuthBar from "@/components/AuthBar";
import "./globals.css";

export const metadata: Metadata = {
  title: "Acme Corp Support Desk",
  description: "Corporate employee support portal",
};

export default function RootLayout({
  children,
}: {
  children: React.ReactNode;
}) {
  return (
    <html lang="en">
      <body>
        <div className="app-shell">
          <AuthBar />
          {children}
        </div>
      </body>
    </html>
  );
}
