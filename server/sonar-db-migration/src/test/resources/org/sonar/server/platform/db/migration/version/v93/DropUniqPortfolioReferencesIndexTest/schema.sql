CREATE TABLE "PORTFOLIO_REFERENCES"(
    "UUID" VARCHAR(40) NOT NULL,
    "PORTFOLIO_UUID" VARCHAR(40) NOT NULL,
    "REFERENCE_UUID" VARCHAR(40) NOT NULL,
    "CREATED_AT" BIGINT NOT NULL
);
ALTER TABLE "PORTFOLIO_REFERENCES" ADD CONSTRAINT "PK_PORTFOLIO_REFERENCES" PRIMARY KEY("UUID");
CREATE UNIQUE INDEX "UNIQ_PORTFOLIO_REFERENCES" ON "PORTFOLIO_REFERENCES"("PORTFOLIO_UUID", "REFERENCE_UUID");
