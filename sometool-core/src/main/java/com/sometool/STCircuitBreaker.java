package com.sometool;


public class STCircuitBreaker {
    private Setting setting;
    private State state = State.Closed;
    private long generation;
    private long expiry;
    private int failures;
    private int all;
    private int consecutiveSuccesses;
    private int consecutiveFailures;

    public STCircuitBreaker() {
        this(new Setting());
    }

    public STCircuitBreaker(Setting setting) {
        this.setting = setting;
    }

    public Token allow() {
        synchronized (this) {
            long now = System.currentTimeMillis();
            StateResult result = currentState(now);
            if (result.state == State.Open) {
                return new Token(false, generation, this);
            }
            return new Token(true, generation, this);
        }
    }

    private void report(long beforeGeneration, boolean success) {
        synchronized (this) {
            long now = System.currentTimeMillis();
            StateResult result = currentState(now);
            if (result.generation != beforeGeneration) {
                return;
            }
            if (success) {
                onSuccess(result.state, now);
            } else {
                onFailure(result.state, now);
            }
        }
    }

    private StateResult currentState(long now) {
        switch (state) {
            case Closed:
                if (expiry < now) {
                    toNewGeneration(now);
                }
                break;
            case Open:
                if (expiry < now) {
                    setState(State.HalfOpen, now);
                }
                break;
        }
        return new StateResult(state, generation);
    }

    private void toNewGeneration(long now) {
        generation++;
        all = 0;
        failures = 0;
        consecutiveSuccesses = 0;

        switch (state) {
            case Closed:
                expiry = now + setting.windowIntervalMs;
                break;
            case Open:
                expiry = now + setting.timeoutMs;
                break;
            case HalfOpen:
                expiry = 0;
                break;
        }
    }

    private void setState(State newState, long now) {
        if (state == newState) {
            return;
        }

        state = newState;
        toNewGeneration(now);
    }

    private void onSuccess(State state, long now) {
        switch (state) {
            case Closed:
                all++;
                consecutiveSuccesses++;
                consecutiveFailures = 0;
                break;
            case HalfOpen:
                all++;
                consecutiveSuccesses++;
                consecutiveFailures = 0;
                if (all - failures > setting.maxRequests) {
                    setState(State.Closed, now);
                }
                break;
        }
    }

    private void onFailure(State state, long now) {
        switch (state) {
            case Closed:
                all++;
                failures++;
                consecutiveSuccesses = 0;
                consecutiveFailures = 0;
                if (readyToOpen()) {
                    setState(State.Open, now);
                }
                break;
            case HalfOpen:
                setState(State.Open, now);
                break;
        }
    }

    private boolean readyToOpen() {
        float failPre = (float) failures / (float) all;
        return (failures >= setting.maxFailNum && failPre >= setting.maxFailPercentage)
                || consecutiveFailures > 5;
    }

    private enum State {
        Closed, HalfOpen, Open,
    }

    public static class Setting {
        // max fail nums
        // the default is 5
        public int maxFailNum = 5;
        // max fail percentage
        // the default is 75%
        public float maxFailPercentage = 0.75f;
        // windowIntervalMs decides when to reset counter if the state is StateClosed
        // the default is 5minutes
        public long windowIntervalMs = 300 * 1000;
        // timeout decides when to turn StateOpen to StateHalfOpen
        // the default is 60s
        public long timeoutMs = 6 * 1000;
        // maxRequests decides when to turn StateHalfOpen to StateClosed
        public int maxRequests;
    }

    private static class StateResult {
        public State state;
        public long generation;

        public StateResult(State state, long generation) {
            this.state = state;
            this.generation = generation;
        }
    }

    public static class Token {
        public boolean allowed;
        private long generation;
        private STCircuitBreaker STCircuitBreaker;

        private Token(boolean allowed, long generation, STCircuitBreaker STCircuitBreaker) {
            this.allowed = allowed;
            this.generation = generation;
            this.STCircuitBreaker = STCircuitBreaker;
        }

        public void report(boolean success) {
            STCircuitBreaker.report(generation, success);
        }
    }
}
