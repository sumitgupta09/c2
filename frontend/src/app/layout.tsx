import type { Metadata } from "next";
import AuthBar from "@/components/AuthBar";
import "./globals.css";

export const metadata: Metadata = {
  title: "Gupta Corp Support Desk",
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
        <div className="scene-bg" aria-hidden="true">
          <div className="scene-orb scene-orb-1" />
          <div className="scene-orb scene-orb-2" />
          <div className="scene-orb scene-orb-3" />
          <div className="scene-grid" />
        </div>
        <div className="app-shell">
          <AuthBar />
          {children}
        </div>
      </body>
    </html>
  );
}
